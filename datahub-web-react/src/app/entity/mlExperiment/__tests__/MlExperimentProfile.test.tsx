import React from 'react';
import { render, waitFor } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';

import { MLExperimentProfile } from '../profile/MLExperimentProfile';
import TestPageContainer from '../../../../utils/test-utils/TestPageContainer';
import { mocks } from '../../../../Mocks';

describe('MlExperimentProfile', () => {
    it('renders', async () => {
        const { getByText, queryAllByText } = render(
            <MockedProvider mocks={mocks} addTypename={false}>
                <TestPageContainer
                    initialEntries={[
                        '/mlExperiment/urn:li:mlExperiment:(urn:li:dataPlatform:sagemaker,trustmodel,PROD)',
                    ]}
                >
                    <MLExperimentProfile urn="urn:li:mlExperiment:(urn:li:dataPlatform:sagemaker,trustmodel,PROD)" />
                </TestPageContainer>
            </MockedProvider>,
        );
        await waitFor(() => expect(queryAllByText('trust experiment').length).toBeGreaterThanOrEqual(1));

        expect(getByText('a ml trust experiment')).toBeInTheDocument();
    });
});
