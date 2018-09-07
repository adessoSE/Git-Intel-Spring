package de.adesso.gitstalker.core.enums;

/**
 * Enum used for the declaration of the request type. Similar to the ResponseProcessor enum but slightly different in cases.
 */
public enum RequestType {
    ORGANIZATION_VALIDATION,
    ORGANIZATION_DETAIL,
    MEMBER_ID,
    MEMBER,
    MEMBER_PR,
    REPOSITORY,
    TEAM,
    EXTERNAL_REPO,
    CREATED_REPOS_BY_MEMBERS
}
