package ru.parceldelivery.common.exception

import ru.parceldelivery.common.models.PDCommand

class UnknownContextCommand(command: PDCommand) : Throwable("Wrong command $command at mapping toTransport stage")
