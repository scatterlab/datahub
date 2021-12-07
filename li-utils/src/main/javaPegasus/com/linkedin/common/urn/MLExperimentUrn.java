package com.linkedin.common.urn;

import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import java.net.URISyntaxException;

import com.linkedin.common.FabricType;

import static com.linkedin.common.urn.UrnUtils.toFabricType;


public final class MLExperimentUrn extends Urn {

  public static final String ENTITY_TYPE = "mlExperiment";

  private final DataPlatformUrn _platform;
  private final String _mlExperimentName;
  private final FabricType _origin;

  public MLExperimentUrn(DataPlatformUrn platform, String mlExperimentName, FabricType origin) {
    super(ENTITY_TYPE, TupleKey.create(platform, mlExperimentName, origin));
    this._platform = platform;
    this._mlExperimentName = mlExperimentName;
    this._origin = origin;
  }

  public DataPlatformUrn getPlatformEntity() {
    return _platform;
  }

  public String getMlExperimentNameEntity() {
    return _mlExperimentName;
  }

  public FabricType getOriginEntity() {
    return _origin;
  }

  public static MLExperimentUrn createFromString(String rawUrn) throws URISyntaxException {
    return createFromUrn(Urn.createFromString(rawUrn));
  }

  public static MLExperimentUrn createFromUrn(Urn urn) throws URISyntaxException {
    if (!"li".equals(urn.getNamespace())) {
      throw new URISyntaxException(urn.toString(), "Urn namespace type should be 'li'.");
    } else if (!ENTITY_TYPE.equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), "Urn entity type should be 'mlExperiment'.");
    } else {
      TupleKey key = urn.getEntityKey();
      if (key.size() != 3) {
        throw new URISyntaxException(urn.toString(), "Invalid number of keys.");
      } else {
        try {
          return new MLExperimentUrn((DataPlatformUrn) key.getAs(0, DataPlatformUrn.class),
              (String) key.getAs(1, String.class), (FabricType) key.getAs(2, FabricType.class));
        } catch (Exception e) {
          throw new URISyntaxException(urn.toString(), "Invalid URN Parameter: '" + e.getMessage());
        }
      }
    }
  }

  public static MLExperimentUrn deserialize(String rawUrn) throws URISyntaxException {
    return createFromString(rawUrn);
  }

  static {
    Custom.initializeCustomClass(DataPlatformUrn.class);
    Custom.initializeCustomClass(FabricType.class);
    Custom.registerCoercer(new DirectCoercer<MLExperimentUrn>() {
      public Object coerceInput(MLExperimentUrn object) throws ClassCastException {
        return object.toString();
      }

      public MLExperimentUrn coerceOutput(Object object) throws TemplateOutputCastException {
        try {
          return MLExperimentUrn.createFromString((String) object);
        } catch (URISyntaxException e) {
          throw new TemplateOutputCastException("Invalid URN syntax: " + e.getMessage(), e);
        }
      }
    }, MLExperimentUrn.class);
  }
}
