package ru.parceldelovery.common.exception

import ru.parceldelovery.common.models.PDCommand

class UnknownContextCommand(command: PDCommand) : Throwable("Wrong command $command at mapping toTransport stage")
