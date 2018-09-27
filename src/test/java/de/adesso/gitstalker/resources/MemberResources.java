package de.adesso.gitstalker.resources;

public class MemberResources {

    public MemberResources() {
    }

    public String getExpectedGeneratedQueryContent() {
        return "{\n" +
                "  node(id: \"MDQ6VXNlcjkxOTkw\") {\n" +
                "    ... on User {\n" +
                "      name\n" +
                "      id\n" +
                "      login\n" +
                "      url\n" +
                "      avatarUrl\n" +
                "      repositoriesContributedTo(first: 100, contributionTypes: COMMIT, includeUserRepositories: true) {\n" +
                "        nodes {\n" +
                "          id\n" +
                "          defaultBranchRef {\n" +
                "            target {\n" +
                "              ... on Commit {\n" +
                "                history(first: 100, since: \"2018-01-01T00:00:00Z\", author: {id: \"MDQ6VXNlcjkxOTkw\"}) {\n" +
                "                  nodes {\n" +
                "                    committedDate\n" +
                "                    url\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "      issues(last: 25) {\n" +
                "        nodes {\n" +
                "          createdAt\n" +
                "          url\n" +
                "        }\n" +
                "      }\n" +
                "      pullRequests(last: 25) {\n" +
                "        nodes {\n" +
                "          createdAt\n" +
                "          url\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  rateLimit {\n" +
                "    cost\n" +
                "    remaining\n" +
                "    resetAt\n" +
                "  }\n" +
                "}";

    }

    public String getExpectedQueryJSONResponse() {
        return "{\n"+
                "  \"data\": {\n"+
                "    \"node\": {\n"+
                "      \"name\": \"Matthias Balke\",\n"+
                "      \"id\": \"MDQ6VXNlcjkxOTkw\",\n"+
                "      \"login\": \"matthiasbalke\",\n"+
                "      \"url\": \"https://github.com/matthiasbalke\",\n"+
                "      \"avatarUrl\": \"https://avatars0.githubusercontent.com/u/91990?v=4\",\n"+
                "      \"repositoriesContributedTo\": {\n"+
                "        \"nodes\": [\n"+
                "          {\n"+
                "            \"id\": \"MDEwOlJlcG9zaXRvcnkxOTg2ODY2Mg==\",\n"+
                "            \"defaultBranchRef\": {\n"+
                "              \"target\": {\n"+
                "                \"history\": {\n"+
                "                  \"nodes\": [\n"+
                "                    {\n"+
                "                      \"committedDate\": \"2018-01-29T12:22:04Z\",\n"+
                "                      \"url\": \"https://github.com/Pentadrago/spring-boot-example-wicket/commit/de9a3a180f1b6cd0f4a735a946de2535dc2af234\"\n"+
                "                    }\n"+
                "                  ]\n"+
                "                }\n"+
                "              }\n"+
                "            }\n"+
                "          },\n"+
                "          {\n"+
                "            \"id\": \"MDEwOlJlcG9zaXRvcnkyNDM3OTcwMw==\",\n"+
                "            \"defaultBranchRef\": {\n"+
                "              \"target\": {\n"+
                "                \"history\": {\n"+
                "                  \"nodes\": [\n"+
                "                    {\n"+
                "                      \"committedDate\": \"2018-01-22T10:35:49Z\",\n"+
                "                      \"url\": \"https://github.com/adessoAG/budgeteer/commit/302997d80466fc378907ddee80c9a02bf951233d\"\n"+
                "                    },\n"+
                "                    {\n"+
                "                      \"committedDate\": \"2018-01-19T12:35:12Z\",\n"+
                "                      \"url\": \"https://github.com/adessoAG/budgeteer/commit/3f80ad69552388b2ca651dfc9221a896bd729fd2\"\n"+
                "                    }\n"+
                "                  ]\n"+
                "                }\n"+
                "              }\n"+
                "            }\n"+
                "          },\n"+
                "          {\n"+
                "            \"id\": \"MDEwOlJlcG9zaXRvcnkzNDY4NTgwMA==\",\n"+
                "            \"defaultBranchRef\": {\n"+
                "              \"target\": {\n"+
                "                \"history\": {\n"+
                "                  \"nodes\": [\n"+
                "                    {\n"+
                "                      \"committedDate\": \"2018-02-26T09:17:03Z\",\n"+
                "                      \"url\": \"https://github.com/diffplug/spotless/commit/2dd2157d777dd2f0ef15d8a649c07fc9a5fa0f32\"\n"+
                "                    }\n"+
                "                  ]\n"+
                "                }\n"+
                "              }\n"+
                "            }\n"+
                "          },\n"+
                "          {\n"+
                "            \"id\": \"MDEwOlJlcG9zaXRvcnk1MDYwMDMyNQ==\",\n"+
                "            \"defaultBranchRef\": {\n"+
                "              \"target\": {\n"+
                "                \"history\": {\n"+
                "                  \"nodes\": [\n"+
                "                    {\n"+
                "                      \"committedDate\": \"2018-01-26T09:23:40Z\",\n"+
                "                      \"url\": \"https://github.com/reflectoring/infiniboard/commit/dd50dd9a5bbe2679a704fa8a7f48aa8f3f29a69d\"\n"+
                "                    }\n"+
                "                  ]\n"+
                "                }\n"+
                "              }\n"+
                "            }\n"+
                "          },\n"+
                "          {\n"+
                "            \"id\": \"MDEwOlJlcG9zaXRvcnk3NDQwMjI4OQ==\",\n"+
                "            \"defaultBranchRef\": {\n"+
                "              \"target\": {\n"+
                "                \"history\": {\n"+
                "                  \"nodes\": []\n"+
                "                }\n"+
                "              }\n"+
                "            }\n"+
                "          },\n"+
                "          {\n"+
                "            \"id\": \"MDEwOlJlcG9zaXRvcnk5MzMwNDUwMA==\",\n"+
                "            \"defaultBranchRef\": {\n"+
                "              \"target\": {\n"+
                "                \"history\": {\n"+
                "                  \"nodes\": []\n"+
                "                }\n"+
                "              }\n"+
                "            }\n"+
                "          }\n"+
                "        ]\n"+
                "      },\n"+
                "      \"issues\": {\n"+
                "        \"nodes\": [\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-04-11T22:18:50Z\",\n"+
                "            \"url\": \"https://github.com/adessoAG/budgeteer/issues/159\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-05-05T11:18:12Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/reflectoring.github.io/issues/68\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-05-12T21:29:31Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/reflectoring.github.io/issues/71\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-05-12T21:37:53Z\",\n"+
                "            \"url\": \"https://github.com/derwebcoder/lead/issues/1\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-06-23T17:18:27Z\",\n"+
                "            \"url\": \"https://github.com/rndevfx/docker-blender-render-cluster/issues/3\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-07-17T10:56:41Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/216\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-08-01T14:34:37Z\",\n"+
                "            \"url\": \"https://github.com/asciidoctor/asciidoctor-intellij-plugin/issues/182\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-26T12:54:49Z\",\n"+
                "            \"url\": \"https://github.com/asciidoctor/asciidoctorj-pdf/issues/12\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-28T09:50:25Z\",\n"+
                "            \"url\": \"https://github.com/asciidoctor/asciidoctor-pdf/issues/863\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-01T12:44:12Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/232\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-05T20:36:06Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/235\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-12-01T09:08:45Z\",\n"+
                "            \"url\": \"https://github.com/diffplug/spotless/issues/169\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-12-08T07:46:38Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/242\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-12-14T19:23:52Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/244\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-01-22T09:51:36Z\",\n"+
                "            \"url\": \"https://github.com/ansible/ansible/issues/35174\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-01-25T20:57:22Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/249\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-06T08:14:25Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/251\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-06T08:27:19Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/252\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-15T17:24:15Z\",\n"+
                "            \"url\": \"https://github.com/diffplug/spotless/issues/204\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-19T07:01:14Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/issues/253\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-19T07:03:20Z\",\n"+
                "            \"url\": \"https://github.com/diffplug/spotless/issues/207\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-27T07:41:40Z\",\n"+
                "            \"url\": \"https://github.com/diffplug/spotless/issues/212\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-03-05T08:28:42Z\",\n"+
                "            \"url\": \"https://github.com/adessoAG/budgeteer/issues/185\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-05-30T07:28:38Z\",\n"+
                "            \"url\": \"https://github.com/adessoAG/budgeteer/issues/220\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-05-30T07:30:02Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/coderadar/issues/173\"\n"+
                "          }\n"+
                "        ]\n"+
                "      },\n"+
                "      \"pullRequests\": {\n"+
                "        \"nodes\": [\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-18T22:29:14Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/223\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-29T16:51:14Z\",\n"+
                "            \"url\": \"https://github.com/jenkinsci/maven-deployment-linker-plugin/pull/4\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-30T15:54:28Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/224\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-31T10:48:59Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/225\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-31T13:08:18Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/226\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-31T13:48:00Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/228\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-10-31T14:26:47Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/229\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-01T09:18:37Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/230\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-01T12:38:55Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/231\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-02T17:53:12Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/233\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-02T18:15:36Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/234\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-09T19:29:04Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/237\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-14T20:08:25Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/reflectoring.github.io/pull/104\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-28T08:25:16Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/238\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-28T09:17:54Z\",\n"+
                "            \"url\": \"https://github.com/matthiasbalke/infiniboard/pull/1\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-28T09:18:24Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/239\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-11-28T09:36:13Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/240\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-12-06T07:38:37Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/241\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-12-14T19:38:28Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/245\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2017-12-14T19:43:19Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/246\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-01-26T09:16:07Z\",\n"+
                "            \"url\": \"https://github.com/reflectoring/infiniboard/pull/250\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-01-29T12:22:11Z\",\n"+
                "            \"url\": \"https://github.com/Pentadrago/spring-boot-example-wicket/pull/4\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-02-21T09:03:55Z\",\n"+
                "            \"url\": \"https://github.com/diffplug/spotless/pull/209\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-03-05T15:32:04Z\",\n"+
                "            \"url\": \"https://github.com/apache/maven-patch-plugin/pull/1\"\n"+
                "          },\n"+
                "          {\n"+
                "            \"createdAt\": \"2018-03-05T15:35:22Z\",\n"+
                "            \"url\": \"https://github.com/apache/maven-patch-plugin/pull/2\"\n"+
                "          }\n"+
                "        ]\n"+
                "      }\n"+
                "    },\n"+
                "    \"rateLimit\": {\n"+
                "      \"cost\": 1,\n"+
                "      \"remaining\": 4999,\n"+
                "      \"resetAt\": \"2018-09-27T12:18:05Z\"\n"+
                "    }\n"+
                "  }\n"+
                "}";
    }
}
