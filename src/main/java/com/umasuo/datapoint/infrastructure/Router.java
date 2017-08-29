package com.umasuo.datapoint.infrastructure;

/**
 * Router.
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
   * Copy root.
   */
  public static final String DATA_COPY = DATA_DEFINITION_ROOT + "/copy";

  /**
   * Platform data definition root.
   */
  public static final String PLATFORM_DATA_ROOT = DATA_DEFINITION_ROOT + "/platform";

  /**
   * The constant PLATFORM_DATA_WITH_ID.
   */
  public static final String PLATFORM_DATA_WITH_ID = PLATFORM_DATA_ROOT + "/{id}";

  /**
   * Developer data definition root.
   */
  public static final String DEVELOPER_DATA_ROOT = DATA_DEFINITION_ROOT + "/developer";

  /**
   * Developer data definition with id.
   */
  public static final String DEVELOPER_DATA_WITH_ID = DEVELOPER_DATA_ROOT + "/{id}";
}
