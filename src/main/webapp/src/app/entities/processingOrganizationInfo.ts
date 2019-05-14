export class ProcessingOrganizationInfo {
    processingMessage: String;
    searchedOrganization: string;
    finishedRequestTypes: String[];
    missingRequestTypes: String[];
    totalCountOfRequestTypes: number;
    totalCountOfNeededRequests: number;
    currentPositionInQueue: number;
}
