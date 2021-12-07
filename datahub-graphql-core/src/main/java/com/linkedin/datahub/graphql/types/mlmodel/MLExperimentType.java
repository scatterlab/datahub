package com.linkedin.datahub.graphql.types.mlmodel;

import com.linkedin.common.urn.Urn;


import com.linkedin.datahub.graphql.types.mappers.BrowseResultMapper;
import com.linkedin.datahub.graphql.types.mappers.UrnSearchResultsMapper;
import com.linkedin.datahub.graphql.types.mlmodel.mappers.MLExperimentSnapshotMapper;
import com.linkedin.entity.client.EntityClient;
import com.linkedin.entity.Entity;
import com.linkedin.metadata.extractor.AspectExtractor;
import com.linkedin.metadata.search.SearchResult;
import com.linkedin.metadata.browse.BrowseResult;
import graphql.execution.DataFetcherResult;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.template.StringArray;

import com.google.common.collect.ImmutableSet;
import com.linkedin.common.urn.MLExperimentUrn;
import com.linkedin.datahub.graphql.QueryContext;
import com.linkedin.datahub.graphql.generated.AutoCompleteResults;
import com.linkedin.datahub.graphql.generated.EntityType;
import com.linkedin.datahub.graphql.generated.FacetFilterInput;
import com.linkedin.datahub.graphql.generated.MLExperiment;
import com.linkedin.datahub.graphql.generated.SearchResults;
import com.linkedin.datahub.graphql.generated.BrowseResults;
import com.linkedin.datahub.graphql.generated.BrowsePath;
import com.linkedin.datahub.graphql.resolvers.ResolverUtils;
import com.linkedin.datahub.graphql.types.BrowsableEntityType;
import com.linkedin.datahub.graphql.types.mappers.BrowsePathsMapper;
import com.linkedin.datahub.graphql.types.SearchableEntityType;
import com.linkedin.datahub.graphql.types.mappers.AutoCompleteResultsMapper;
import com.linkedin.metadata.query.AutoCompleteResult;
import static com.linkedin.datahub.graphql.Constants.BROWSE_PATH_DELIMITER;

public class MLExperimentType implements SearchableEntityType<MLExperiment>, BrowsableEntityType<MLExperiment> {

    private static final Set<String> FACET_FIELDS = ImmutableSet.of("origin", "platform");
    private final EntityClient _entityClient;

    public MLExperimentType(final EntityClient entityClient) {
        _entityClient = entityClient;
    }

    @Override
    public EntityType type() {
        return EntityType.MLEXPERIMENT;
    }

    @Override
    public Class<MLExperiment> objectClass() {
        return MLExperiment.class;
    }

    @Override
    public List<DataFetcherResult<MLExperiment>> batchLoad(final List<String> urns, final QueryContext context) throws Exception {
        final List<MLExperimentUrn> mlExperimentUrns = urns.stream()
            .map(MLModelUtils::getMLExperimentUrn)
            .collect(Collectors.toList());

        try {
            final Map<Urn, Entity> mlExperimentMap = _entityClient.batchGet(mlExperimentUrns
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()),
            context.getAuthentication());

            final List<Entity> gmsResults = mlExperimentUrns.stream()
                .map(modelUrn -> mlExperimentMap.getOrDefault(modelUrn, null)).collect(Collectors.toList());

            return gmsResults.stream()
                .map(gmsMlExperiment -> gmsMlExperiment == null ? null
                    : DataFetcherResult.<MLExperiment>newResult()
                        .data(MLExperimentSnapshotMapper.map(gmsMlExperiment.getValue().getMLExperimentSnapshot()))
                        .localContext(AspectExtractor.extractAspects(gmsMlExperiment.getValue().getMLExperimentSnapshot()))
                        .build())
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to batch load MLExperiments", e);
        }
    }

    @Override
    public SearchResults search(@Nonnull String query,
                                @Nullable List<FacetFilterInput> filters,
                                int start,
                                int count,
                                @Nonnull final QueryContext context) throws Exception {
        final Map<String, String> facetFilters = ResolverUtils.buildFacetFilters(filters, FACET_FIELDS);
        final SearchResult searchResult = _entityClient.search("mlExperiment", query, facetFilters, start, count, context.getAuthentication());
        return UrnSearchResultsMapper.map(searchResult);
    }

    @Override
    public AutoCompleteResults autoComplete(@Nonnull String query,
                                            @Nullable String field,
                                            @Nullable List<FacetFilterInput> filters,
                                            int limit,
                                            @Nonnull final QueryContext context) throws Exception {
        final Map<String, String> facetFilters = ResolverUtils.buildFacetFilters(filters, FACET_FIELDS);
        final AutoCompleteResult result = _entityClient.autoComplete("mlExperiment", query, facetFilters, limit, context.getAuthentication());
        return AutoCompleteResultsMapper.map(result);
    }

    @Override
    public BrowseResults browse(@Nonnull List<String> path,
                                @Nullable List<FacetFilterInput> filters,
                                int start,
                                int count,
                                @Nonnull final QueryContext context) throws Exception {
        final Map<String, String> facetFilters = ResolverUtils.buildFacetFilters(filters, FACET_FIELDS);
        final String pathStr = path.size() > 0 ? BROWSE_PATH_DELIMITER + String.join(BROWSE_PATH_DELIMITER, path) : "";
        final BrowseResult result = _entityClient.browse(
                "mlExperiment",
                pathStr,
                facetFilters,
                start,
                count,
            context.getAuthentication());
        return BrowseResultMapper.map(result);
    }

    @Override
    public List<BrowsePath> browsePaths(@Nonnull String urn, @Nonnull final QueryContext context) throws Exception {
        final StringArray result = _entityClient.getBrowsePaths(MLModelUtils.getMLExperimentUrn(urn), context.getAuthentication());
        return BrowsePathsMapper.map(result);
    }
}
