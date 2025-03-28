// Copyright by camLine GmbH, Germany, 2022
package com.fii.practic.mes.admin.general.error;

import lombok.Getter;

public enum ServerErrorEnum {

	UNEXPECTED_EXCEPTION(500, "An unexpected exception happened. %s", "more details"),
	EXTERNAL_EXCEPTION_MESSAGE_NOT_FOUND(500, "External message could not be found. Please check the logs in the external server for more information"),
	NOT_IMPLEMENTED(500, "Not implemented"),
	OPTIMISTIC_LOCK_ERROR(422, "Error locking %s.\nA newer version exist, please update and try again!", Constants.ENTITY_CLASS),
	OPTIMISTIC_LOCK_ERROR_ID(422, "Error locking %s with id %s.\nA newer version exist, please update and try again!", Constants.ENTITY_CLASS, Constants.ENTITY_ID),
	OPTIMISTIC_LOCK_ERROR_NAMED(422, "Error locking %s with name %s.\nA newer version exist, please update and try again!", Constants.ENTITY_CLASS, Constants.ENTITY_NAME),
	FIND_ERROR(404, "Error finding %s.", Constants.ENTITY_CLASS),
	FIND_ERROR_BY_ID(404, "Error finding %s with id %s.", Constants.ENTITY_CLASS, Constants.ENTITY_ID),
	FIND_ERROR_NAMED(404, "Error finding %s with name %s.", Constants.ENTITY_CLASS, Constants.ENTITY_NAME),
	FIND_ERROR_BY_FIELD(404, "Error finding %s by field %s equals %s", Constants.ENTITY_CLASS, Constants.FIELD_NAME, "field_value"),

	FK_NAME_ALREADY_IN_USE(422, "An entity with specified NAME already exist"),
	FK_NAME_ALREADY_IN_USE_FOR_ENTITY(422, "An <%s> entity with specified NAME already exist", Constants.ENTITY_NAME),
	FK_UUID_ALREADY_IN_USE(422, "A entity with specified UUID already exist"),
	FK_SPECIFIC_CONSTRAINT_NAME_RECEIVED(422, "Specific constraint name exception thrown"),
	FK_CONSTRAINT_VIOLATION_UNKNOWN(422, "Unknown foreign key constraint <%s> violation happen.", "constraint_name"),
	CONSTRAINT_VIOLATION_UNKNOWN(422, "Unknown constraint <%s> violation happen.", "constraint_name"),

	VALIDATOR_CONSTRAINT_VIOLATION(422, "%s", Constants.MESSAGE),
	UPDATE_NAME_NOT_ALLOWED(406, "The server denied the request to update <%s> name.", Constants.ENTITY_NAME),
	UPDATE_FIELD_NOT_ALLOWED(406, "The server denied the request to update <%s> %s.", Constants.ENTITY_NAME, Constants.PROPERTY_NAME),
	DELETE_ENTITY_FAIL(406, "Delete <%s> <%s - %s> failed.", Constants.ENTITY_NAME, Constants.ENTITY_ID, Constants.ENTITY_VERSION),

	UUID_NAME_NOT_MATCH(422, "<%s> object with uuid: <%s> have another name", Constants.ENTITY_NAME, Constants.ENTITY_ID),
	QUERY_INVALID_SORT_FIELD(400, "Invalid query sort <%s>", Constants.PROPERTY_NAME),
	QUERY_INVALID_SORT_MISSING_ASC_DESC(400, "Invalid query sort. " +
			"Missing ascending/descending sorting characters [asc:/desc:] for <%s> property", Constants.PROPERTY_NAME),
	QUERY_INVALID_FILTER(400, "Invalid query filter <%s>", Constants.PROPERTY_NAME),
	QUERY_INVALID_FILTER_LOGICAL_OPERATOR_WITH_VALUES(400, "Invalid query filter <%s>. Logical operator doesn't accept values.", Constants.PROPERTY_NAME),
	QUERY_INVALID_FILTER_UNARY_OPERATOR_MULTI_VALUES(400, "Invalid query filter <%s>. Multiple values found for unary operator.", Constants.PROPERTY_NAME),
	QUERY_INVALID_FILTER_MULTI_OPERATOR_NO_VALUES(400, "Invalid query filter <%s>. No values found for multi operator.", Constants.PROPERTY_NAME),

	;

	@Getter
    private final int code;
	@Getter
	private final String fallBackText;
	private final Object[] arguments;

	ServerErrorEnum(final int code, final String fallBackText, final Object... args) {
		this.code = code;
		this.fallBackText = fallBackText;
		this.arguments = args;
	}

	private static class Constants {
		public static final String ENTITY_CLASS = "entity_class";
		public static final String ENTITY_ID = "entity_uuid";
		public static final String ENTITY_VERSION = "entity_version";
		public static final String ENTITY_NAME = "entity_name";
		public static final String FIELD_NAME = "field_name";
		public static final String PROPERTY_NAME = "propertyName";
		public static final String MESSAGE = "message";
		public static final String OBJECT = "object";
	}
}
