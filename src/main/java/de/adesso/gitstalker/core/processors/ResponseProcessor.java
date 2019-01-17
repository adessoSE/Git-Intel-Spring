package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.REST.OrganizationController;
import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.config.RateLimitConfig;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.ChartJSData;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class ResponseProcessor {

    private Date dateToStartCrawling = new Date(System.currentTimeMillis() - Config.PAST_DAYS_TO_CRAWL_IN_MS);
    @Transient
    private Logger logger = LoggerFactory.getLogger(ResponseProcessor.class);

    public void updateRateLimit(RateLimit rateLimit, RequestType requestType) {
        RateLimitConfig.setRemainingRateLimit(rateLimit.getRemaining());
        RateLimitConfig.setResetRateLimitAt(rateLimit.getResetAt());
        RateLimitConfig.addPreviousRequestCostAndRequestType(rateLimit.getCost(), requestType);

        logger.info("Rate Limit remaining: " + RateLimitConfig.getRemainingRateLimit());
    }

    public ChartJSData generateChartJSData(ArrayList<Calendar> arrayOfDates) {
        this.sortArrayOfDatesAscendingOrder(arrayOfDates);

        if (arrayOfDates.isEmpty()) {
            return this.processEmptyChartJSData(arrayOfDates);
        } else return this.processValidChartJSData(arrayOfDates);

    }

    private String getFormattedDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    private void sortArrayOfDatesAscendingOrder(ArrayList<Calendar> arrayOfCalendars) {
        Collections.sort(arrayOfCalendars, Comparator.naturalOrder());
    }

    private ChartJSData processEmptyChartJSData(ArrayList<Calendar> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (int x = 0; x != Config.PAST_DAYS_AMOUNT_TO_CRAWL + 1; x++) {
            chartJSLabels.add(this.getFormattedDate(new Date(dateToStartCrawling.getTime() + Config.DAY_IN_MS * x)));
            chartJSDataset.add(0);
        }
        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private ChartJSData processValidChartJSData(ArrayList<Calendar> arrayOfCalendars) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (Calendar calendar : arrayOfCalendars) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            this.processStartDateChartJSData(chartJSLabels, chartJSDataset, arrayOfCalendars, calendar, formatter);
            this.processChartJSData(chartJSLabels, chartJSDataset, calendar);
            this.processGapsBetweenChartJSData(chartJSLabels, chartJSDataset, arrayOfCalendars, calendar, formatter);
            this.processEndDateChartJSData(chartJSLabels, chartJSDataset, arrayOfCalendars, calendar);
        }

        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private void processEndDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendars, Calendar selectedCalendar) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.HOUR, 0);

        if (arrayOfCalendars.size() - 1 == arrayOfCalendars.indexOf(selectedCalendar) && currentCalendar.getTimeInMillis() > selectedCalendar.getTimeInMillis()) {
            for (long x = (((currentCalendar.getTimeInMillis() - selectedCalendar.getTimeInMillis()) / Config.DAY_IN_MS)); x >= 0; x--) {
                chartJSLabels.add(this.getFormattedDate(new Date(currentCalendar.getTimeInMillis() - Config.DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processStartDateChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendars, Calendar selectedCalendar, DateFormat formatter) {
        Date selectedDateFormatted = null;
        Date oneWeekAgoFormatted = null;

        try {
            selectedDateFormatted = formatter.parse(formatter.format(selectedCalendar.getTime()));
            oneWeekAgoFormatted = formatter.parse(formatter.format(dateToStartCrawling));
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        if (oneWeekAgoFormatted.getTime() < selectedDateFormatted.getTime() && arrayOfCalendars.indexOf(selectedCalendar) == 0) {
            for (int x = 0; x < (((selectedDateFormatted.getTime() - oneWeekAgoFormatted.getTime()) / Config.DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(oneWeekAgoFormatted.getTime() + Config.DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    private void processChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, Calendar selectedCalendar) {
        String formattedDate = this.getFormattedDate(selectedCalendar.getTime());
        if (!chartJSLabels.contains(formattedDate)) {
            chartJSLabels.add(formattedDate);
            chartJSDataset.add(1);
        } else {
            chartJSDataset.set(chartJSLabels.indexOf(formattedDate), chartJSDataset.get(chartJSLabels.indexOf(formattedDate)) + 1);
        }
    }

    private void processGapsBetweenChartJSData(ArrayList<String> chartJSLabels, ArrayList<Integer> chartJSDataset, ArrayList<Calendar> arrayOfCalendar, Calendar selectedCalendar, DateFormat formatter) {
        if (arrayOfCalendar.size() != arrayOfCalendar.indexOf(selectedCalendar) + 1) {
            Date selectedDateFormatted = null;
            Date followingDateInArrayFormatted = arrayOfCalendar.get(arrayOfCalendar.indexOf(selectedCalendar) + 1).getTime();
            try {
                selectedDateFormatted = formatter.parse(formatter.format(selectedCalendar.getTime()));
                followingDateInArrayFormatted = formatter.parse(formatter.format(followingDateInArrayFormatted));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int x = 1; x < (((followingDateInArrayFormatted.getTime() - selectedDateFormatted.getTime()) / Config.DAY_IN_MS)); x++) {
                chartJSLabels.add(this.getFormattedDate(new Date(selectedDateFormatted.getTime() + Config.DAY_IN_MS * x)));
                chartJSDataset.add(0);
            }
        }
    }

    /**
     * Calculates the internal commits of the members in the own organization repositories. Generated as ChartJSData and saved in OrganizationDetail.
     *
     * @param organization
     */
    public void calculateInternalOrganizationCommitsChartJSData(OrganizationWrapper organization, HashMap<String, ArrayList<Calendar>> committedRepo) {
        Set<String> organizationRepoIDs = organization.getRepositories().keySet();
        ArrayList<Calendar> internalCommitsDates = new ArrayList<>();
        for (String id : organizationRepoIDs) {
            if (committedRepo.containsKey(id)) {
                internalCommitsDates.addAll(committedRepo.get(id));
            }
        }

        organization.getOrganizationDetail().setInternalRepositoriesCommits(this.generateChartJSData(internalCommitsDates));

    }

    /**
     * Calculates the external pull requests of the members. Generated as ChartJSData and saved in OrganizationDetail.
     *
     * @param organization
     */
    public void calculateExternalOrganizationPullRequestsChartJSData(OrganizationWrapper organization, HashMap<String, ArrayList<Calendar>> contributedRepos) {
        Set<String> organizationRepoIDs = organization.getRepositories().keySet();
        ArrayList<Calendar> externalPullRequestsDates = new ArrayList<>();
        contributedRepos.keySet().removeAll(organizationRepoIDs);
        for (ArrayList<Calendar> calendar : contributedRepos.values()) {
            externalPullRequestsDates.addAll(calendar);
        }

        organization.getOrganizationDetail().setExternalRepositoriesPullRequests(this.generateChartJSData(externalPullRequestsDates));
    }

    public HashMap<String, ArrayList<String>> calculateExternalRepoContributions(OrganizationWrapper organization) {
        HashMap<String, ArrayList<String>> externalContributions = new HashMap<>();
        externalContributions.putAll(organization.getMemberPRRepoIDs());
        externalContributions.keySet().removeAll(organization.getRepositories().keySet());
        return externalContributions;
    }

    private void checkIfOrganizationUpdateIsFinished(OrganizationWrapper organization, OrganizationRepository organizationRepository, ProcessingRepository processingRepository) {
        if (organization.getFinishedRequests().size() == RequestType.values().length) {
            organization.setLastUpdateTimestamp(new Date());
            this.removeFinishedResponseProcessors(organization.getOrganizationName());
            processingRepository.deleteByInternalOrganizationName(organization.getOrganizationName());
            logger.info("Complete Update Cost: " + organization.getCompleteUpdateCost());
        }
        organizationRepository.save(organization);
    }

    private void removeFinishedResponseProcessors(String organizationName) {
        ResponseProcessorManager.organizationValidationProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.organizationDetailProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.memberIDProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.memberProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.memberPRProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.repositoryProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.teamProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.externalRepoProcessorHashMap.remove(organizationName);
        ResponseProcessorManager.createdReposByMembersProcessorHashMap.remove(organizationName);
    }

    public boolean checkIfQueryIsLastOfRequestType(OrganizationWrapper organization, Query processingQuery, RequestType requestType, RequestRepository requestRepository) {
        if (requestRepository.findByQueryRequestTypeAndOrganizationName(requestType, processingQuery.getOrganizationName()).size() == 1) {
            organization.addFinishedRequest(requestType);
            return true;
        }
        return false;
    }


    public void doFinishingQueryProcedure(RequestRepository requestRepository, OrganizationRepository organizationRepository, ProcessingRepository processingRepository, OrganizationWrapper organization, Query processingQuery, RequestType requestType) {
        organizationRepository.save(organization);
        organization.setCompleteUpdateCost(RateLimitConfig.getPreviousRequestCostAndRequestType().get(requestType));
        this.checkIfQueryIsLastOfRequestType(organization, processingQuery, requestType, requestRepository);
        this.checkIfOrganizationUpdateIsFinished(organization, organizationRepository, processingRepository);
    }


    public void generateNextRequests(String organizationName, String endCursor, RequestType requestType, RequestRepository requestRepository) {
        requestRepository.save(new RequestManager()
                .setOrganizationName(organizationName)
                .setEndCursor(endCursor)
                .generateRequest(requestType));
    }

    public boolean checkIfRequestTypeIsFinished(OrganizationWrapper organization, RequestType requestType) {
        return organization.getFinishedRequests().contains(requestType);
    }

}
