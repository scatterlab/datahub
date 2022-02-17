import React from 'react';
import styled from 'styled-components';
import { EntityType, MlExperiment } from '../../../../types.generated';
import DefaultPreviewCard from '../../../preview/DefaultPreviewCard';
import { capitalizeFirstLetter } from '../../../shared/textUtil';
import { useEntityRegistry } from '../../../useEntityRegistry';
import { IconStyleType } from '../../Entity';

const LogoContainer = styled.div`
    padding-right: 8px;
`;

export const Preview = ({ model }: { model: MlExperiment }): JSX.Element => {
    const entityRegistry = useEntityRegistry();
    const capitalPlatformName = capitalizeFirstLetter(model?.platform?.name || '');

    return (
        <DefaultPreviewCard
            url={entityRegistry.getEntityUrl(EntityType.Mlexperiment, model.urn)}
            name={model.name || ''}
            description={model.description || ''}
            type={entityRegistry.getEntityName(EntityType.Mlexperiment)}
            logoComponent={
                <LogoContainer>
                    {entityRegistry.getIcon(EntityType.Mlexperiment, 20, IconStyleType.HIGHLIGHT)}
                </LogoContainer>
            }
            platform={capitalPlatformName}
            qualifier={model.origin}
            tags={model.globalTags || undefined}
            owners={model?.ownership?.owners}
        />
    );
};
