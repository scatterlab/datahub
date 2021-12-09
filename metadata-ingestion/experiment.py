from typing import List

import datahub.emitter.mce_builder as builder
from datahub.emitter.mcp import MetadataChangeProposalWrapper
from datahub.metadata.com.linkedin.pegasus2avro.mxe import MetadataChangeEvent
from datahub.emitter.rest_emitter import DatahubRestEmitter
from datahub.metadata.schema_classes import (
    ChangeTypeClass,
    MLExperimentKeyClass,
    MLExperimentPropertiesClass,
    MLExperimentSnapshotClass,
    VersionTagClass,
)

urn = "urn:li:mlExperiment:(urn:li:dataPlatform:mlflow,testexp,PROD)"
experiment = MLExperimentPropertiesClass(
    description="WASANS",
    date=123456,
    version=VersionTagClass("VERSION_TAG"),
    trainingDatasets=["urn:li:dataset:(urn:li:dataPlatform:bigquery,pingpong-internal.KLUE.train,PROD)"]
)

snapshot = MLExperimentSnapshotClass(urn, [experiment])
mce = MetadataChangeEvent(proposedSnapshot=snapshot)

# Create an emitter to the GMS REST API.
emitter = DatahubRestEmitter("http://localhost:8088")

# Emit metadata!
emitter.emit_mce(mce=mce)
