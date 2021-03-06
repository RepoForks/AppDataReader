package com.awesomedroidapps.inappstoragereader;

import java.util.Arrays;
import java.util.List;

/**
 * A file for storing all the Constants.
 * Created by anshul on 11/2/17.
 */

public class Constants {

  public static final String SHARED_PREFERENCES_KEY = "KEY";
  public static final String SHARED_PREFERENCES_VALUE = "VALUE";
  public static final String SHARED_PREFERENCES_TYPE = "TYPE";


  public static final String BUNDLE_QUERY_REQUEST = "bundleQueryRequest";
  public static final String BUNDLE_TABLE_INFO = "bundleTableInfo";
  public static final int REQUEST_CODE_WHERE_CLAUSE = 0;
  public static final int REQUEST_CODE_SET_CLAUSE = 1;
  public static final int REQUEST_CODE_VALUES_CLAUSE = 2;
  public static final String BUNDLE_REQUEST_CODE = "requestCode";
  public static final String BUNDLE_CONTENT_VALUES = "contentValues";


  public static final String BUNDLE_DATABASE_NAME = "databaseName";
  public static final String BUNDLE_TABLE_NAME = "tableName";
  public static final String BUNDLE_RAW_QUERY = "rawQuery";
  public static final String SHARED_PREFERENCES_NAME = "Shared Preferences";
  public static final String BUNDLE_FILE_NAME = "fileName";
  public static final String BUNDLE_DISPLAY_FILTER = "bundle_display_filter";
  public static final String BUNDLE_WHERE_CLAUSE = "whereClause";
  public static final String OPENING_BRACKET = "(";
  public static final String CLOSING_BRACKET = ")";
  public static final String SPACE = " ";
  public static final int ZERO_INDEX = 0;
  public static final String EMPTY_STRING = "";
  public static final int INVALID_RESPONSE = -1;
  public static final String FROM_PREFIX = "FROM";
  public static final String INTO_PREFIX = "INTO";
  public static final String COMMA = ",";
  public static final String EQUAL = "=";
  public static final String INVERTED_COMMA = "'";
  public static final String AND = "AND";
  public static final String WHERE_CLAUSE = "WHERE";
  public static final String WHERE_CONDITION = "WHERE CONDITION";
  public static final String COLUMN_NAMES = "COLUMN NAMES";
  public static final String INSERT_CLAUSE = "INSERT";
  public static final String SET_CLAUSE = "SET";
  public static final String SET_VALUES = "SET VALUES";
  public static final String VALUES = "VALUES";
  public static final String ASTERIK = "*";

  public static final String DIALOG_COPY = "COPY";
  public static final String DIALOG_EDIT = "EDIT";
  public static final String DIALOG_DONE = "DONE";

  public static final String PRAGMA_COLUMN_PK = "pk";
  public static final String PRAGMA_COLUMN_CID = "cid";
  public static final String PRAGMA_COLUMN_TYPE = "type";
  public static final int COLUMN_PRIMARY_KEY_VALUE = 1;

  public static final String DIALOG_OK_TEXT = "OK";
  public static final String DIALOG_CANCEL_TEXT = "CANCEL";
  public static final String QUERY_AFFECTED_ROWS = "affected_row_count";
  public static final String IS_NULL = "is null";
  public static final String INVALID_STRING = "INVALID";

  public static final List integerTypes = Arrays.asList(new String[]{
      "int", "integer", "tinyint", "smallint", "mediumint",
      "bigint", "unsigned bigint", "int2", "int8"
  });

  public static final List floatTypes = Arrays.asList(new String[]{
      "real", "double", "double precision", "float", "numeric",
      "decimal(10,5)", "boolean", "date", "datetime"
  });

  public static final List textTypes = Arrays.asList(new String[]{
      "character(20)", "varchar(255)", "varying character(255)",
          "nchar(55)", "native character(70)", "nvarchar(100)", "text"
  });

  public static final List blobTypes = Arrays.asList(new String[]{"blob", "clob"});

}
