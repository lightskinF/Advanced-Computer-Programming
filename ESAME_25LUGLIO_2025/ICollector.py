from abc import ABC, abstractmethod

class ICollector(ABC):
    @abstractmethod 
    def measure(self, stringa_comando, float_comando):
        pass