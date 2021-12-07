package com.linkedin.datahub.graphql.types.mlmodel.mappers;

import com.linkedin.datahub.graphql.types.common.mappers.StringMapMapper;
import java.util.stream.Collectors;

import com.linkedin.common.urn.Urn;
import com.linkedin.datahub.graphql.generated.MLExperimentProperties;
import com.linkedin.datahub.graphql.types.mappers.ModelMapper;

import lombok.NonNull;

public class MLExperimentPropertiesMapper implements ModelMapper<com.linkedin.ml.metadata.MLExperimentProperties, MLExperimentProperties> {

    public static final MLExperimentPropertiesMapper INSTANCE = new MLExperimentPropertiesMapper();

    public static MLExperimentProperties map(@NonNull final com.linkedin.ml.metadata.MLExperimentProperties mlExperimentProperties) {
        return INSTANCE.apply(mlExperimentProperties);
    }

    @Override
    public MLExperimentProperties apply(@NonNull final com.linkedin.ml.metadata.MLExperimentProperties mlExperimentProperties) {
        final MLExperimentProperties result = new MLExperimentProperties();

        result.setDate(mlExperimentProperties.getDate());
        result.setDescription(mlExperimentProperties.getDescription());
        if (mlExperimentProperties.getVersion() != null) {
            result.setVersion(mlExperimentProperties.getVersion().getVersionTag());
        }
        result.setType(mlExperimentProperties.getType());
        if (mlExperimentProperties.getHyperParams() != null) {
            result.setHyperParams(mlExperimentProperties.getHyperParams().stream().map(
                param -> MLHyperParamMapper.map(param)).collect(Collectors.toList()));
        }

        result.setCustomProperties(StringMapMapper.map(mlExperimentProperties.getCustomProperties()));

        if (mlExperimentProperties.getMetrics() != null) {
            result.setMetrics(mlExperimentProperties.getMetrics().stream().map(metric ->
                MLMetricMapper.map(metric)
            ).collect(Collectors.toList()));
        }

        result.setTags(mlExperimentProperties.getTags());

        return result;
    }
}
