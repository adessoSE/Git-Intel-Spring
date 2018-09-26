package de.adesso.gitstalker.resources;

public class MemberIDResources {

    public MemberIDResources() {

    }

    public String getExpectedGeneratedQueryContent() {
        return "query {\n" +
                "organization(login: \"adessoAG\") {\n" +
                "members(first: 100 after: testEndCursor) {\n" +
                "pageInfo {\n" +
                " hasNextPage\n" +
                "endCursor\n" +
                "}\n" +
                "nodes {\n" +
                "id\n" +
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

    public String getResponseMemberIDRequest() {
        return "{\n" +
                "  \"data\": {\n" +
                "    \"organization\": {\n" +
                "      \"members\": {\n" +
                "        \"pageInfo\": {\n" +
                "          \"hasNextPage\": true,\n" +
                "          \"endCursor\": \"Y3Vyc29yOnYyOpHOAlyCJg==\"\n" +
                "        },\n" +
                "        \"nodes\": [\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjkxOTkw\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI3NTI2OA==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM0OTE4OA==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjQyMDc0MQ==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjYxNzg0Nw==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjY4MDc0MA==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjY4NjY5Mg==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjcyMDE5OQ==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjc3ODUzNg==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjk1MjczMA==\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEyMDA0MjM=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEyNDY1NjY=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEzNjM1Nzg=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE0MDEwMzg=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE0MDE4Mjk=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE2NjY1NDk=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE2Njg4NTU=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIyOTczNzI=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIzOTIyMTc=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjMxNTUxNzA=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjMzNTE4Njg=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjQ2OTAxNTA=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjUwNTcwMTI=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjUxNTQ3NTA=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjUzOTA4OTk=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjU3MjkzNzY=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjU3OTY5NjI=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjU4Njk3NzA=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjYwODkxMjI=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjY1OTcwMDg=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjY3Mjg2MDE=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjc1OTAxNjM=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjc4NDU5MDM=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjgzOTQ5Mjk=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjg1NjgwMzU=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjkwNzY5MDE=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjkxMDk4MDM=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjkyMDkzNDQ=\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEwMjE4OTQ0\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjExMDEwMzEw\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEyMjAxODM1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEyNDU4NTk5\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEzMTM0Nzgw\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEzMzM4MzQz\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjEzODE5NDgw\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE0ODM0NDk1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE1NjA5NDkz\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE1NjcwMDIz\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE1NjcyMjA4\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE2MDY5NzUx\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE2MzUwMjYz\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE2ODE4MTcx\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE3MjAyNzMz\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE4MjYzNzc0\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE5ODYxOTk4\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjE5OTk2ODU4\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIwMDc1MDQ0\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIwMzk3OTcx\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIwNDgzNDAx\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIzMDg2OTgy\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIzMjc1MjIy\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIzNDI0NTM4\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjIzNTUyNDAx\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI0NTIwOTI3\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI0OTkxMzA5\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI1MzAwODIx\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI1MzQzMjE1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI2MDAyNDY3\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI4MzgzNTMw\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjI5MjAxNTUy\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjMwODI4MjI2\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjMyNTYyNDI2\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjMzMjUyMzA1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM0MDI4MDY5\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM0NzA4NDYy\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM1MDIzMDgz\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM5MTkwNjY4\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM5MTkyMjg2\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"MDQ6VXNlcjM5NjE3MDYy\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"rateLimit\": {\n" +
                "      \"cost\": 1,\n" +
                "      \"remaining\": 4999,\n" +
                "      \"resetAt\": \"2018-09-25T09:31:07Z\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}