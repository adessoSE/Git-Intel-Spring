package de.adesso.gitstalker.resources;

public class ExternalRepoResources {

    public ExternalRepoResources() {
    }

    public String getExpectedGeneratedQueryContent(String formattedRepoIDs, String DateToStartCrawling) {
        String expectedGeneratedQueryContent = "{\n" +
                //Request for ten repositories combined
                "nodes(ids: [" + formattedRepoIDs + "]) {\n" +
                "... on Repository {\n" +
                "url\n" +
                "id\n" +
                "name\n" +
                "description\n" +
                "forkCount\n" +
                "stargazers {\n" +
                "totalCount\n" +
                "}\n" +
                "licenseInfo {\n" +
                "name\n" +
                "}\n" +
                "primaryLanguage {\n" +
                "name\n" +
                "}\n" +
                "defaultBranchRef {\n" +
                "target {\n" +
                "... on Commit {\n" +
                "history(first: 50, since: \"" + DateToStartCrawling + "\") {\n" +
                "nodes {\n" +
                "committedDate\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "pullRequests(last: 50) {\n" +
                "nodes {\n" +
                "createdAt\n" +
                "}\n" +
                "}\n" +
                "issues(last: 50) {\n" +
                "nodes {\n" +
                "createdAt\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "rateLimit {\n" +
                "cost\n" +
                "remaining\n" +
                "resetAt\n" +
                "}\n" +
                "}";

        return expectedGeneratedQueryContent;
    }

    public String getResponseExternalRepoRequest() {
        return "{\n" +
                "  \"data\": {\n" +
                "    \"nodes\": [\n" +
                "      {\n" +
                "        \"url\": \"https://github.com/matthiasbalke/magento\",\n" +
                "        \"id\": \"MDEwOlJlcG9zaXRvcnk2NDkzMzI=\",\n" +
                "        \"name\": \"magento\",\n" +
                "        \"description\": \"Magento Modules\",\n" +
                "        \"forkCount\": 0,\n" +
                "        \"stargazers\": {\n" +
                "          \"totalCount\": 1\n" +
                "        },\n" +
                "        \"licenseInfo\": null,\n" +
                "        \"primaryLanguage\": null,\n" +
                "        \"defaultBranchRef\": {\n" +
                "          \"target\": {\n" +
                "            \"history\": {\n" +
                "              \"nodes\": []\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"pullRequests\": {\n" +
                "          \"nodes\": []\n" +
                "        },\n" +
                "        \"issues\": {\n" +
                "          \"nodes\": [\n" +
                "            {\n" +
                "              \"createdAt\": \"2012-07-10T18:43:03Z\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"url\": \"https://github.com/matthiasbalke/artikel-bdd-2013\",\n" +
                "        \"id\": \"MDEwOlJlcG9zaXRvcnkxMDk0NjQ1Mw==\",\n" +
                "        \"name\": \"artikel-bdd-2013\",\n" +
                "        \"description\": \"Example project for the article 'Behavior Driven Development' by Matthias Balke und Sebastian Laag\",\n" +
                "        \"forkCount\": 0,\n" +
                "        \"stargazers\": {\n" +
                "          \"totalCount\": 0\n" +
                "        },\n" +
                "        \"licenseInfo\": null,\n" +
                "        \"primaryLanguage\": {\n" +
                "          \"name\": \"Java\"\n" +
                "        },\n" +
                "        \"defaultBranchRef\": {\n" +
                "          \"target\": {\n" +
                "            \"history\": {\n" +
                "              \"nodes\": []\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"pullRequests\": {\n" +
                "          \"nodes\": []\n" +
                "        },\n" +
                "        \"issues\": {\n" +
                "          \"nodes\": []\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"rateLimit\": {\n" +
                "      \"cost\": 1,\n" +
                "      \"remaining\": 4999,\n" +
                "      \"resetAt\": \"2018-09-25T06:44:16Z\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
