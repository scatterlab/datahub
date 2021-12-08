import * as React from 'react';
import { CodeSandboxOutlined } from '@ant-design/icons';
import { MlExperiment, EntityType, SearchResult, RelationshipDirection } from '../../../types.generated';
import { Preview } from './preview/Preview';
import { MLExperimentProfile } from './profile/MLExperimentProfile';
import { Entity, IconStyleType, PreviewType } from '../Entity';
import { getDataForEntityType } from '../shared/containers/profile/utils';
import { getChildrenFromRelationships } from '../../lineage/utils/getChildren';

/**
 * Definition of the DataHub MlModel entity.
 */
export class MLExperimentEntity implements Entity<MlExperiment> {
    type: EntityType = EntityType.Mlexperiment;

    icon = (fontSize: number, styleType: IconStyleType) => {
        if (styleType === IconStyleType.TAB_VIEW) {
            return <CodeSandboxOutlined style={{ fontSize }} />;
        }

        if (styleType === IconStyleType.HIGHLIGHT) {
            return <CodeSandboxOutlined style={{ fontSize, color: '#9633b9' }} />;
        }

        return (
            <CodeSandboxOutlined
                style={{
                    fontSize,
                    color: '#BFBFBF',
                }}
            />
        );
    };

    isSearchEnabled = () => true;

    isBrowseEnabled = () => true;

    isLineageEnabled = () => true;

    getAutoCompleteFieldName = () => 'name';

    getPathName = () => 'mlExperiments';

    getEntityName = () => 'ML Experiment';

    getCollectionName = () => 'ML Experiments';

    renderProfile = (urn: string) => <MLExperimentProfile urn={urn} />;

    renderPreview = (_: PreviewType, data: MlExperiment) => {
        return <Preview model={data} />;
    };

    renderSearch = (result: SearchResult) => {
        const data = result.entity as MlExperiment;
        return <Preview model={data} />;
    };

    getLineageVizConfig = (entity: MlExperiment) => {
        return {
            urn: entity.urn,
            name: entity.name,
            type: EntityType.Mlexperiment,
            downstreamChildren: getChildrenFromRelationships({
                // eslint-disable-next-line @typescript-eslint/dot-notation
                incomingRelationships: entity?.['incoming'],
                // eslint-disable-next-line @typescript-eslint/dot-notation
                outgoingRelationships: entity?.['outgoing'],
                direction: RelationshipDirection.Incoming,
            }),
            upstreamChildren: getChildrenFromRelationships({
                // eslint-disable-next-line @typescript-eslint/dot-notation
                incomingRelationships: entity?.['incoming'],
                // eslint-disable-next-line @typescript-eslint/dot-notation
                outgoingRelationships: entity?.['outgoing'],
                direction: RelationshipDirection.Outgoing,
            }),
            icon: entity.platform?.info?.logoUrl || undefined,
            platform: entity.platform?.name,
        };
    };

    displayName = (data: MlExperiment) => {
        return data.name;
    };

    getGenericEntityProperties = (mlModel: MlExperiment) => {
        return getDataForEntityType({ data: mlModel, entityType: this.type, getOverrideProperties: (data) => data });
    };
}
