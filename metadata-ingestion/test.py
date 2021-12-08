from typing import List

import datahub.emitter.mce_builder as builder
from datahub.emitter.mcp import MetadataChangeProposalWrapper
from datahub.emitter.rest_emitter import DatahubRestEmitter
from datahub.metadata.schema_classes import (
    ChangeTypeClass,
    MLExperimentKeyClass,
    MLExperimentPropertiesClass,
    VersionTagClass,
)

# Construct upstream tables.
upstream_tables = []

# Construct a lineage object.
experiment = MLExperimentPropertiesClass(
    description="WASANS",
    date=123456,
    version=VersionTagClass("VERSION_TAG"),
)

# Construct a MetadataChangeProposalWrapper object.
lineage_mcp = MetadataChangeProposalWrapper(
    entityType="mlExperiment",
    changeType=ChangeTypeClass.UPSERT,
    entityUrn="urn:li:mlExperiment:(urn:li:dataPlatform:mlflow,testexp,PROD)",
    aspectName="mlExperimentProperties",
    aspect=experiment,
)

# Create an emitter to the GMS REST API.
emitter = DatahubRestEmitter("http://localhost:8088")

# Emit metadata!
emitter.emit_mcp(lineage_mcp)
