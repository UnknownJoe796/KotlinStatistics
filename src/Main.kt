/**
 * Created by josep on 11/16/2015.
 */

fun slitSeparation(wavelength:Double, surfaceDistance:StatNum, separation:StatNum): StatNum {
    return wavelength * surfaceDistance / separation;
}

fun slitWidth(wavelength:Double, surfaceDistance:StatNum, separation:StatNum): StatNum {
    return wavelength * surfaceDistance / separation;
}

fun slitsPerUnitUsingFirstOrderOnly(wavelength:Double, surfaceDistance:StatNum, separation:StatNum): StatNum {
    var hypotenuse = (separation * separation + surfaceDistance * surfaceDistance).squareRoot()
    return separation / (hypotenuse * 1.0 * wavelength)
}

fun presentResult(name:String, unit:String, result:StatNum, actual:Number){
    println("The $name was ${result.toString()} $unit, and the range two standard errors out is ${result.sigFig - result.error * 2} $unit to ${result.sigFig + result.error * 2} $unit.")
    val formattedActual = SigFig(actual.toDouble()).toString()
    if(actual.toDouble() in result.range95Percent()){
        println("The actual value of $formattedActual $unit is within this range.")
    } else {
        println("The actual value of $formattedActual $unit is not within this range.")
    }
    println()
}

fun main(vararg arguments:String){

    val separationsA = arrayListOf(.0041, .0042, .004, .0041, .0039, .0042, .0039, .0093, .0043, .0038);
    val separationsB = arrayListOf(.0065, .009, .0081, .0089, .0091, .0079, .0084, .0079, .0088, .0103);
    val separationsE = arrayListOf(.0041, .0042, .004, .004, .0039, .0038, .0043, .004, .0041, .0039);
    val separationsF = arrayListOf(.008, .0076, .0073, .0078, .0079, .007, .0072, .0075, .0071, .0075);
    val separationsDiffractionGrate = arrayListOf(.9595, .986);
    var surfaceDistance = StatNum(2.3612, .001);
    val wavelength = 632.8e-9;

    val actualSlitSeparationA = .351e-3;
    val actualSlitSeparationB = .176e-3;
    val actualSlitsPerUnit = 400000.0;
    val actualSlitWidthE = .351e-3;
    val actualSlitWidthF = .176e-3;

    val separationA = mean(separationsA, -4)
    val separationB = mean(separationsB, -4)
    val separationE = mean(separationsE, -4)
    val separationF = mean(separationsF, -4)
    val separationDiffractionGrate = mean(separationsDiffractionGrate)

    println(separationA)

    //val slitSeparationA = slitSeparation(wavelength, surfaceDistance, separationA)
    presentResult("slit separation of A", "mm", slitSeparation(wavelength, surfaceDistance, separationA) * 1000.0, actualSlitSeparationA * 1000)
    presentResult("slit separation of B", "mm", slitSeparation(wavelength, surfaceDistance, separationB) * 1000.0, actualSlitSeparationB * 1000)
    presentResult("slits of the diffraction grate", "slits/mm", slitsPerUnitUsingFirstOrderOnly(wavelength, surfaceDistance, separationDiffractionGrate) / 1000.0, actualSlitsPerUnit / 1000)
    presentResult("slit width of E", "mm", slitWidth(wavelength, surfaceDistance, separationE) * 1000.0, actualSlitWidthE * 1000)
    presentResult("slit width of F", "mm", slitWidth(wavelength, surfaceDistance, separationF) * 1000.0, actualSlitWidthF * 1000)
}