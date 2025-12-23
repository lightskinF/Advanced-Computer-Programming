from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class Prodotto(_message.Message):
    __slots__ = ("serial_number",)
    SERIAL_NUMBER_FIELD_NUMBER: _ClassVar[int]
    serial_number: int
    def __init__(self, serial_number: _Optional[int] = ...) -> None: ...

class Ack(_message.Message):
    __slots__ = ("esito",)
    ESITO_FIELD_NUMBER: _ClassVar[int]
    esito: bool
    def __init__(self, esito: bool = ...) -> None: ...

class Vuoto(_message.Message):
    __slots__ = ("messaggio",)
    MESSAGGIO_FIELD_NUMBER: _ClassVar[int]
    messaggio: str
    def __init__(self, messaggio: _Optional[str] = ...) -> None: ...
