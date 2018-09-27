package de.adesso.gitstalker.resources;

public class CreatedReposByMemberResources {

    public CreatedReposByMemberResources() {
    }

    public String getResponseCreatedReposByMemberRequest() {
        return "{\n" +
                "  \"data\": {\n" +
                "    \"node\": {\n" +
                "      \"id\": \"MDQ6VXNlcjkxOTkw\",\n" +
                "      \"repositories\": {\n" +
                "        \"pageInfo\": {\n" +
                "          \"hasNextPage\": true,\n" +
                "          \"endCursor\": \"Y3Vyc29yOnYyOpHOAAnodA==\"\n" +
                "        },\n" +
                "        \"nodes\": [\n" +
                "          {\n" +
                "            \"url\": \"https://github.com/matthiasbalke/magento\",\n" +
                "            \"id\": \"MDEwOlJlcG9zaXRvcnk2NDkzMzI=\",\n" +
                "            \"name\": \"magento\",\n" +
                "            \"description\": \"Magento Modules\",\n" +
                "            \"forkCount\": 0,\n" +
                "            \"stargazers\": {\n" +
                "              \"totalCount\": 1\n" +
                "            },\n" +
                "            \"licenseInfo\": null,\n" +
                "            \"primaryLanguage\": null,\n" +
                "            \"isFork\": false,\n" +
                "            \"isMirror\": false,\n" +
                "            \"owner\": {\n" +
                "              \"id\": \"MDQ6VXNlcjkxOTkw\"\n" +
                "            }\n" +
                "          },\n" +
                "{\n" +
                "            \"url\": \"https://github.com/matthiasbalke/magento\",\n" +
                "            \"id\": \"MDEwOlJlcG9zaXRvcnk2NDkzMzI=\",\n" +
                "            \"name\": \"magento\",\n" +
                "            \"description\": \"Magento Modules\",\n" +
                "            \"forkCount\": 0,\n" +
                "            \"stargazers\": {\n" +
                "              \"totalCount\": 1\n" +
                "            },\n" +
                "            \"licenseInfo\": null,\n" +
                "            \"primaryLanguage\": null,\n" +
                "            \"isFork\": true,\n" +
                "            \"isMirror\": false,\n" +
                "            \"owner\": {\n" +
                "              \"id\": \"MDQ6VXNlcjkxOTkw\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"rateLimit\": {\n" +
                "      \"cost\": 1,\n" +
                "      \"remaining\": 4999,\n" +
                "      \"resetAt\": \"2018-09-20T10:08:49Z\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    public String getExpectedQueryJSONResponse() {
        return "{\n" +
                "node(id: \"testMemberID\") {\n" +
                "... on User {\n" +
                "id\n" +
                "repositories(first: 100 after: testEndCursor) {\n" +
                "pageInfo {\n" +
                "hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
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
                "isFork\n" +
                "isMirror\n" +
                "owner {\n" +
                "id\n" +
                "}\n" +
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
    }
}
