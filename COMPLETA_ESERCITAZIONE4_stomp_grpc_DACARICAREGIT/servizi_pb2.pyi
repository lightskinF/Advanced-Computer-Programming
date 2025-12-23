from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class Articolo(_message.Message):
    __slots__ = ("id", "prodotto")
    ID_FIELD_NUMBER: _ClassVar[int]
    PRODOTTO_FIELD_NUMBER: _ClassVar[int]
    id: int
    prodotto: str
    def __init__(self, id: _Optional[int] = ..., prodotto: _Optional[str] = ...) -> None: ...

class Esito(_message.Message):
    __slots__ = ("messaggio",)
    MESSAGGIO_FIELD_NUMBER: _ClassVar[int]
    messaggio: str
    def __init__(self, messaggio: _Optional[str] = ...) -> None: ...

class Richiesta(_message.Message):
    __slots__ = ("messaggio",)
    MESSAGGIO_FIELD_NUMBER: _ClassVar[int]
    messaggio: str
    def __init__(self, messaggio: _Optional[str] = ...) -> None: ...
