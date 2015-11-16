/**
 * Created by josep on 11/16/2015.
 */

fun mean(numbers:Collection<Number>): StatNum {
    var sum = 0.0;
    for (value in numbers) {
        sum += value.toDouble()
    }
    val av = sum / numbers.size

    var deltaSquaredSum = 0.0
    for (value in numbers) {
        val delta = value.toDouble() - av
        deltaSquaredSum += delta * delta
    }
    val dev = Math.sqrt(deltaSquaredSum / (numbers.size - 1))

    return StatNum(av, dev / Math.sqrt(numbers.size.toDouble()))
}

fun mean(numbers:Collection<Number>, leastSigFig:Int): StatNum {
    var sum = 0.0;
    for (value in numbers) {
        sum += value.toDouble()
    }
    val av = sum / numbers.size

    var deltaSquaredSum = 0.0
    for (value in numbers) {
        val delta = value.toDouble() - av
        deltaSquaredSum += delta * delta
    }
    val dev = Math.sqrt(deltaSquaredSum / (numbers.size - 1))

    return StatNum(SigFig(av, leastSigFig), dev / Math.sqrt(numbers.size.toDouble()))
}

fun error(measured:Number, actual:Number):Double{
    return Math.abs(measured.toDouble() - actual.toDouble()) / Math.abs(actual.toDouble())
}