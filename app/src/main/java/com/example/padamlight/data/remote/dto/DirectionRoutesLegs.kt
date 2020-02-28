package com.example.padamlight.data.remote.dto

class DirectionRoutesLegs {
    var duration: DirectionRoutesLegsDuration? = null
    var start_location: DirectionRoutesLegsStart_location? = null
    var distance: DirectionRoutesLegsDistance? = null
    var start_address: String? = null
    var end_location: DirectionRoutesLegsEnd_location? = null
    var end_address: String? = null
    var steps: Array<DirectionRoutesLegsSteps>? = null
}
