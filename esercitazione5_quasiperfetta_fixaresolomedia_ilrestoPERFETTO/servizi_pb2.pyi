from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class Empty(_message.Message):
    __slots__ = ("messaggio",)
    MESSAGGIO_FIELD_NUMBER: _ClassVar[int]
    messaggio: str
    def __init__(self, messaggio: _Optional[str] = ...) -> None: ...

class Sensor(_message.Message):
    __slots__ = ("id", "data_type")
    ID_FIELD_NUMBER: _ClassVar[int]
    DATA_TYPE_FIELD_NUMBER: _ClassVar[int]
    id: int
    data_type: str
    def __init__(self, id: _Optional[int] = ..., data_type: _Optional[str] = ...) -> None: ...

class MeanRequest(_message.Message):
    __slots__ = ("sensor_id", "data_type")
    SENSOR_ID_FIELD_NUMBER: _ClassVar[int]
    DATA_TYPE_FIELD_NUMBER: _ClassVar[int]
    sensor_id: int
    data_type: str
    def __init__(self, sensor_id: _Optional[int] = ..., data_type: _Optional[str] = ...) -> None: ...

class StringMessage(_message.Message):
    __slots__ = ("calcolo_media",)
    CALCOLO_MEDIA_FIELD_NUMBER: _ClassVar[int]
    calcolo_media: float
    def __init__(self, calcolo_media: _Optional[float] = ...) -> None: ...
