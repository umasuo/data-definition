package com.umasuo.datapoint.infrastructure;

/**
 * Created by umasuo on 17/3/7.
 */
public class Router {

  /**
   * data definition root.
   */
  public static final String DATA_DEFINITION_ROOT = "/data-definitions";
  
  /**
   * definition with id.
   */
  public static final String DATA_DEFINITION_WITH_ID = DATA_DEFINITION_ROOT + "/{id}";

  /**
   * verify url.
   */
  public static final String DATA_VERIFY = DATA_DEFINITION_WITH_ID + "/verify";

  /**
   * verify url.
   */
  public static final String DATA_VERIFY_WITH_DATA_ID = DATA_DEFINITION_ROOT + "/verify";

  /**
   * Copy root.
   */
  public static final String DATA_COPY = DATA_DEFINITION_ROOT + "/copy";

  /**
   * The constant OPEN_DATA_DEFINITION.
   */
  public static final String OPEN_DATA_DEFINITION = DATA_DEFINITION_ROOT + "/open";

  public static final String PLATFORM_DATA_ROOT = DATA_DEFINITION_ROOT + "/platform";

  public static final String DEVELOPER_DATA_ROOT = DATA_DEFINITION_ROOT + "/developer";

  public static final String DEVELOPER_DATA_WITH_ID = DEVELOPER_DATA_ROOT + "/{id}";
}
