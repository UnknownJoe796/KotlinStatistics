/**
 * Created by josep on 11/16/2015.
 */
class StatNum(
        val sigFig:SigFig,
        val error:Double
): Number(), Comparable<Number> by sigFig {

    val value:Double get() = sigFig.value

    override fun toDouble(): Double = value.toDouble()
    override fun toFloat(): Float = value.toFloat()
    override fun toLong(): Long  = value.toLong()
    override fun toInt(): Int  = value.toInt()
    override fun toChar(): Char  = value.toChar()
    override fun toShort(): Short  = value.toShort()
    override fun toByte(): Byte  = value.toByte()

    constructor(value:Double, error:Double):this(SigFig(value), error)

    val relativeError:Double get(){
        return error / Math.abs(value)
    }

    operator fun plus(other: StatNum): StatNum {
        return StatNum(
                sigFig + other.sigFig,
                Math.sqrt(error * error + other.error * other.error)
        )
    }
    operator fun minus(other: StatNum): StatNum {
        return StatNum(
                sigFig - other.sigFig,
                Math.sqrt(error * error + other.error * other.error)
        )
    }
    operator fun times(other: StatNum): StatNum {
        val result = sigFig * other.sigFig
        return StatNum(
                result,
                Math.abs(result.value) * Math.sqrt(relativeError * relativeError + other.relativeError * other.relativeError)
        )
    }
    operator fun div(other: StatNum): StatNum {
        val result = sigFig / other.sigFig
        return StatNum(
                result,
                Math.abs(result.value) * Math.sqrt(relativeError * relativeError + other.relativeError * other.relativeError)
        )
    }

    operator fun plus(other:Double): StatNum {
        return StatNum(
                sigFig + other,
                error
        )
    }
    operator fun minus(other:Double): StatNum {
        return StatNum(
                sigFig - other,
                error
        )
    }
    operator fun times(other:Double): StatNum {
        return StatNum(
                sigFig * other,
                error * other
        )
    }
    operator fun div(other:Double): StatNum {
        return StatNum(
                sigFig / other,
                error / other
        )
    }

    operator fun plus(other:SigFig): StatNum {
        return StatNum(
                sigFig + other,
                error
        )
    }
    operator fun minus(other:SigFig): StatNum {
        return StatNum(
                sigFig - other,
                error
        )
    }
    operator fun times(other:SigFig): StatNum {
        return StatNum(
                sigFig * other,
                error * other.value
        )
    }
    operator fun div(other:SigFig): StatNum {
        return StatNum(
                sigFig / other,
                error / other.value
        )
    }

    fun pow(power:Double):StatNum{
        val result = sigFig.pow(power)
        return StatNum(
                result,
                result.value * error / value
        )
    }

    fun squared():StatNum = pow(2.0)
    fun squareRoot():StatNum = pow(.5)

    fun simpleFormat():String{
        return sigFig.toString() + " +/- " + SigFig(error, sigFig.leastSignificantPosition).toString()
    }

    override fun toString(): String {
        val newLeast = Math.max(sigFig.leastSignificantPosition, error.mostSignificantPosition)
        val newSigFig = SigFig(sigFig.value, sigFig.mostSignificantPosition, newLeast)
        val newError = SigFig(error, sigFig.mostSignificantPosition, newLeast)
        return newSigFig.toString() + " +/- " + newError.toString()
    }

    fun printRange95Percent(unit:String = ""): String {
        return "(${sigFig - (error * 2)} $unit, ${sigFig + (error * 2)} $unit)"
        //return sigFig.toString() + " +/- " + SigFig(error, sigFig.leastSignificantPosition).toString()
    }

    fun range95Percent():Range<Double>{
        return (sigFig - (error * 2)).toDouble() .. (sigFig + (error * 2)).toDouble()
    }

    fun detailedFormat(): String {
        return sigFig.value.toString() + " +/- " + error.toString()
    }
}

operator fun Double.plus(other:StatNum): StatNum {
    return StatNum(
            SigFig(this) + other.sigFig,
            other.error
    )
}
operator fun Double.minus(other:StatNum): StatNum {
    return StatNum(
            SigFig(this) - other.sigFig,
            other.error
    )
}
operator fun Double.times(other:StatNum): StatNum {
    return StatNum(
            SigFig(this) * other.sigFig,
            this * other.error
    )
}
operator fun Double.div(other:StatNum): StatNum {
    return StatNum(
            SigFig(this) / other.sigFig,
            this / other.error
    )
}