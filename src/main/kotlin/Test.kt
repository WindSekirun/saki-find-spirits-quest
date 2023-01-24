import kotlin.math.round
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

/*
 * 탐험 점수에 따라 정령을 선택할 때, 목표로 할 최소 정령값.
 * 만일 최소 U등급을 목표로 한다면 6000
 */
var pickMinValue = 6800

/**
 * 무기/정령/보석 에 대해 최대한 높은 값으로 선택하려고 할 때, 목표로 할 값
 * goalCount: 최종으로 선택할 후보군 갯수 (높을수록 다양한 선택지를 선택)
 * goalMinValue: 후보군의 최소 점수. 무기/정령/보석을 U/UUU/UUU 로 맞추고 싶다면 20000 (UUU=7000, U=6000)
 */
var goalCount = 1
var goalMinValue = 21000

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("No data file present.")
        println("Usage: java -jar saki-find-spirits-quest-1.0.jar data.json")
        return
    }

    val data = Json.decodeFromString<Data>(File(args.first()).readText())
    pickMinValue = data.pickMinValue
    goalCount = data.goalCount
    goalMinValue = data.goalMinValue

    val spiritsList = buildList {
        this.addAll(data.spiritsList.common)
        this.addAll(data.spiritsList.advanced)
        this.addAll(data.spiritsList.hero)
        this.addAll(data.spiritsList.legend)
        this.addAll(data.spiritsList.god)
        this.addAll(data.spiritsList.immortal)
    }

    val start = System.currentTimeMillis() / 1000.0
    val (first, second, third) = findBestValue(spiritsList)
    val end = System.currentTimeMillis() / 1000.0
    val message = """
        Found Best value (${round(end - start)}s)
        Spirits Total: ${spiritsList.sum()}
        Total: ${first.sum() + second.sum() + third.sum()}
        #1: ${first.sum()} - $first
        #2: ${second.sum()} - $second
        #3: ${third.sum()} - $third
    """.trimIndent()
    println(message)
}

/**
 * [pickRandomSpirits] 의 선택값 부터 목표로 하는 점수군 선택
 * @return 설정한 목표에 최대로 도달한 값
 */
private fun findBestValue(spiritsList: List<Int>): Triple<List<Int>, List<Int>, List<Int>> {
    var tryCount = 0
    val result = buildList {
        while (this.size != goalCount) {
            val triple = pickRandomSpirits(spiritsList, tryCount++)
            val (first, second, third) = triple
            val sum = first.sum() + second.sum() + third.sum()
            if (sum in goalMinValue..21000) {
                this += triple
            }
        }
    }.sortedByDescending { it.first.sum() + it.second.sum() + it.third.sum() }
    return result.first()
}

/**
 * [pickMinValue] 부터 7000 까지 범위에 들어오는 가상 선택값 3개를 반환
 * @return [1950, 1450, 1350, 900, 700, 650], [2000, 1400, 1400, 850, 750, 600], [1900, 1850, 1000, 950, 800, 500]
 */
private fun pickRandomSpirits(spiritsList: List<Int>, count: Int): Triple<List<Int>, List<Int>, List<Int>> {
    var list = spiritsList.toMutableList()
    val result = mutableListOf<List<Int>>()
    var tryCount = 0
    var retryCount = 0
    while (result.size != 3) {
        val pick = list.shuffled().take(6)
        val sum = pick.sum()
        if (sum in pickMinValue..7000) {
            list -= pick.toSet()
            result += pick
        }
        if (tryCount - retryCount >= 50000) {
            retryCount += 50000
            result.clear()
            list = spiritsList.toMutableList()
        }
        println("Try #$count-${tryCount++}, $result - ${result.map { it.sum() }}")
    }
    return Triple(result[0].sortedDescending(), result[1].sortedDescending(), result[2].sortedDescending())
}

@Serializable
data class Data (
    val goalCount: Int,
    val goalMinValue: Int,
    val pickMinValue: Int,
    val spiritsList: SpiritsList
)

@Serializable
data class SpiritsList (
    val common: List<Int>,
    val advanced: List<Int>,
    val hero: List<Int>,
    val legend: List<Int>,
    val god: List<Int>,
    val immortal: List<Int>
)