import React from 'react';
import { Space, Table, Typography } from 'antd';
import { MlHyperParam, MlMetric, MlExperiment } from '../../../../types.generated';

export type Props = {
    model?: MlExperiment;
};

export default function MLExperimentSummary({ model }: Props) {
    const propertyTableColumns = [
        {
            title: 'Name',
            dataIndex: 'name',
            width: 450,
        },
        {
            title: 'Value',
            dataIndex: 'value',
        },
    ];

    return (
        <Space direction="vertical" style={{ width: '100%' }} size="large">
            <Typography.Title level={3}>Metrics</Typography.Title>
            <Table
                pagination={false}
                columns={propertyTableColumns}
                dataSource={model?.properties?.metrics as MlMetric[]}
            />
            <Typography.Title level={3}>Hyper Parameters</Typography.Title>
            <Table
                pagination={false}
                columns={propertyTableColumns}
                dataSource={model?.properties?.hyperParams as MlHyperParam[]}
            />
        </Space>
    );
}
