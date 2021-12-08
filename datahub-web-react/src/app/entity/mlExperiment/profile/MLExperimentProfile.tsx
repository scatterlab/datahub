import React from 'react';
import { Alert } from 'antd';
import { useGetMlExperimentQuery } from '../../../../graphql/mlExperiment.generated';
import { LegacyEntityProfile } from '../../../shared/LegacyEntityProfile';
import { MlExperiment, EntityType } from '../../../../types.generated';
import MLExperimentHeader from './MLExperimentHeader';
import { Message } from '../../../shared/Message';
import { Ownership as OwnershipView } from '../../shared/components/legacy/Ownership';
import { useEntityRegistry } from '../../../useEntityRegistry';
import analytics, { EventType } from '../../../analytics';
import MLExperimentSummary from './MLExperimentSummary';
import { Properties } from '../../shared/components/legacy/Properties';

const EMPTY_ARR: never[] = [];

export enum TabType {
    Summary = 'Summary',
    Groups = 'Groups',
    Deployments = 'Deployments',
    Features = 'Features',
    Ownership = 'Ownership',
    CustomProperties = 'Properties',
}

/**
 * Responsible for display the MLModel Page
 */
export const MLExperimentProfile = ({ urn }: { urn: string }): JSX.Element => {
    const entityRegistry = useEntityRegistry();
    const { loading, error, data } = useGetMlExperimentQuery({ variables: { urn } });

    if (error || (!loading && !error && !data)) {
        return <Alert type="error" message={error?.message || 'Entity failed to load'} />;
    }
    const getHeader = (mlExperiment: MlExperiment) => <MLExperimentHeader mlExperiment={mlExperiment} />;

    const getTabs = (model: MlExperiment) => {
        return [
            {
                name: TabType.Summary,
                path: TabType.Summary.toLowerCase(),
                content: <MLExperimentSummary model={model} />,
            },
            {
                name: TabType.CustomProperties,
                path: TabType.CustomProperties.toLowerCase(),
                content: <Properties properties={model?.properties?.customProperties || EMPTY_ARR} />,
            },
            // {
            //     name: TabType.Deployments,
            //     path: TabType.Deployments.toLowerCase(),
            //     content: <SourcesView features={features} />,
            // },
            {
                name: TabType.Ownership,
                path: TabType.Ownership.toLowerCase(),
                content: (
                    <OwnershipView
                        owners={model?.ownership?.owners || []}
                        lastModifiedAt={model?.ownership?.lastModified?.time || 0}
                    />
                ),
            },
        ];
    };

    return (
        <>
            {loading && <Message type="loading" content="Loading..." style={{ marginTop: '10%' }} />}
            {data && data.mlExperiment && (
                <LegacyEntityProfile
                    titleLink={`/${entityRegistry.getPathName(EntityType.Mlexperiment)}/${urn}`}
                    title={data.mlExperiment?.name || ''}
                    tabs={getTabs(data.mlExperiment as MlExperiment)}
                    header={getHeader(data.mlExperiment as MlExperiment)}
                    onTabChange={(tab: string) => {
                        analytics.event({
                            type: EventType.EntitySectionViewEvent,
                            entityType: EntityType.Mlexperiment,
                            entityUrn: urn,
                            section: tab,
                        });
                    }}
                />
            )}
        </>
    );
};
