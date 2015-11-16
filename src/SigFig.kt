import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.text.Regex

/**
 * Created by josep on 11/16/2015.
 */
class SigFig(
        val value: Double,
        val mostSignificantPosition: Int,
        val leastSignificantPosition: Int
): Number(), Comparable<Number> {

    override fun toDouble(): Double = value.toDouble()
    override fun toFloat(): Float = value.toFloat()
    override fun toLong(): Long  = value.toLong()
    override fun toInt(): Int  = value.toInt()
    override fun toChar(): Char  = value.toChar()
    override fun toShort(): Short  = value.toShort()
    override fun toByte(): Byte  = value.toByte()

    override fun compareTo(other: Number): Int {
        return value.compareTo(other.toDouble())
    }

    val sigFigs: Int by lazy {
        mostSignificantPosition - leastSignificantPosition + 1
    }

    companion object{
        fun parse(value:String):SigFig{
            val actualValue = value.toDouble()
            val most = actualValue.mostSignificantPosition
            var num = 0;
            for(char in value){
                if(char >= '0' || char <= '9') num++
                if(char == 'e' || char=='E') break;
            }

            return SigFig(actualValue, most, most - num + 1)
        }
        fun precise(value: Double):SigFig{
            return SigFig(value, -1000)
        }
    }

    constructor(value: Double):this(value, value.mostSignificantPosition, value.guessLeastSignificantPosition)
    constructor(value: Double, leastSignificantPosition: Int):this(value, value.mostSignificantPosition, leastSignificantPosition)

    operator fun plus(other: SigFig): SigFig {
        return SigFig(
                value + other.value,
                Math.max(mostSignificantPosition, other.mostSignificantPosition),
                Math.max(leastSignificantPosition, other.leastSignificantPosition)
        )
    }

    operator fun minus(other: SigFig): SigFig {
        return SigFig(
                value - other.value,
                Math.max(mostSignificantPosition, other.mostSignificantPosition),
                Math.max(leastSignificantPosition, other.leastSignificantPosition)
        )
    }
    operator fun times(other: SigFig): SigFig {
        val newSigFigs = Math.min(sigFigs, other.sigFigs)
        val result = value * other.value
        val most = result.mostSignificantPosition
        return SigFig(
                result,
                most,
                most - newSigFigs + 1
        )
    }
    operator fun div(other: SigFig): SigFig {
        val newSigFigs = Math.min(sigFigs, other.sigFigs)
        val result = value / other.value
        val most = result.mostSignificantPosition
        return SigFig(
                result,
                most,
                most - newSigFigs + 1
        )
    }

    operator fun plus(other: Double): SigFig {
        return this + SigFig.precise(other)
    }

    operator fun minus(other: Double): SigFig {
        return this - SigFig.precise(other)
    }
    operator fun times(other: Double): SigFig {
        return this * SigFig.precise(other)
    }
    operator fun div(other: Double): SigFig {
        return this / SigFig.precise(other)
    }

    fun pow(power:Double):SigFig{
        val result = Math.pow(value, power)
        val most = result.mostSignificantPosition
        return SigFig(
                result,
                most,
                most - sigFigs + 1
        )
    }

    fun squared():SigFig = pow(2.0)
    fun squareRoot():SigFig = pow(.5)

    override fun toString(): String {
        val formatString = buildString {
            if(4 > mostSignificantPosition && mostSignificantPosition > -4){
                for(i in mostSignificantPosition downTo Math.max(leastSignificantPosition, 0)){
                    append('0')
                }
                if(leastSignificantPosition < 0) {
                    append('.')
                    for(i in -1 downTo Math.min(leastSignificantPosition, 0)){
                        append('0')
                    }
                }
            } else {
                if(sigFigs == 1){
                    append('0')
                } else {
                    append("0.")
                    for(i in 2 .. sigFigs){
                        append('0')
                    }
                }
                append("E0")
            }
        }
        val format = DecimalFormat(formatString)
        return format.format(value)
    }
}
val Double.mostSignificantPosition: Int get() {
    if (this == 0.0) return 0
    var guess = 0
    while (true) {
        val num = this / Math.pow(10.0, guess.toDouble())
        if (num < 1) {
            guess--
        } else if (num > 10) {
            guess++
        } else {
            break;
        }
    }
    return guess;
}

val Double.guessLeastSignificantPosition: Int get() {
    val smaller: Double = .00000000001;
    val small: Double = .0000000001;
    if (this == 0.0) return 0
    var guess = 0
    while (true) {
        val remainder = (this + smaller) % Math.pow(10.0, (guess + 1).toDouble())
        val smallerRemainder = (this + smaller) % Math.pow(10.0, (guess).toDouble())
        if (smallerRemainder > small) {
            guess--
        } else if (remainder < small) {
            guess++
        } else {
            break;
        }
    }
    return guess;
}


/*val Double.guessLeastSignificantPosition: Int get() {
    val str = toString()
    val decPos = str.indexOf('.')
    val ePos = str.indexOf('E');
    if(ePos != -1){
        val e = str.substring(ePos + 1).toInt()
        return Math.max(e - (ePos - 2), -9)
    } else if(decPos != -1) {
        var index = 0
        var final = 0
        for(char in str.substring(decPos + 1)){
            if(index > 9) break
            if(char != '0'){
                final = index
            }
            index++
        }
        return -(final + 1)
    } else {
        var final = 0
        for(i in str.length - 1 downTo 0){
            if(str[i] != '0'){
                break;
            }
            final++
        }
        return final
    }
}*/