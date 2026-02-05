package com.childproduct.designassistant.test

import com.childproduct.designassistant.database.entity.EceEnvelope

/**
 * ECE Envelope 数据测试
 * 验证Envelope数据的完整性和准确性
 */
object EceEnvelopeDataTest {

    /**
     * 测试所有标准Envelope数据
     */
    fun testStandardEnvelopes() {
        println("========== ECE Envelope 数据测试 ==========")

        val envelopes = EceEnvelope.STANDARD_ENVELOPES

        // 1. 验证数量
        assert(envelopes.size == 4) {
            "Expected 4 envelopes, got ${envelopes.size}"
        }
        println("✓ Envelope数量正确: ${envelopes.size}")

        // 2. 验证Size Class
        val sizeClasses = envelopes.map { it.sizeClass }.sorted()
        val expectedSizeClasses = listOf("B1", "B2", "D", "E")
        assert(sizeClasses == expectedSizeClasses) {
            "Expected size classes $expectedSizeClasses, got $sizeClasses"
        }
        println("✓ Size Class正确: $sizeClasses")

        // 3. 验证每个Envelope的尺寸
        envelopes.forEach { envelope ->
            println("\n--- Size Class: ${envelope.sizeClass} ---")
            println("  产品组: ${envelope.applicableGroup}")
            println("  外形尺寸: ${envelope.maxLengthMm} × ${envelope.maxWidthMm} × ${envelope.maxHeightMm} mm")
            println("  座舱尺寸: ${envelope.minCockpitLengthMm} × ${envelope.minCockpitWidthMm} mm")
            println("  头枕高度: ${envelope.minHeadRestHeightMm}-${envelope.maxHeadRestHeightMm} mm")
            println("  ISOFIX宽度: ${envelope.isofixWidthMm} mm")
            println("  Top Tether距离: ${envelope.topTetherDistanceMm} mm")
            println("  支撑腿占地: ${envelope.legFootprintMm} mm")
            println("  侧防宽度: ${envelope.sideImpactWidthMm} mm")

            // 验证尺寸符合ISOFIX标准
            assert(envelope.validateIsSizeClass()) {
                "Size Class ${envelope.sizeClass} 尺寸不符合ISOFIX标准"
            }
            println("  ✓ 尺寸符合ISOFIX标准")

            // 验证适用的假人代码
            val dummyCodes = envelope.getCompatibleDummyCodes()
            println("  适用假人: $dummyCodes")
            assert(dummyCodes.isNotEmpty()) {
                "Size Class ${envelope.sizeClass} 没有适用的假人"
            }
        }

        // 4. 测试根据产品组获取Envelope
        println("\n--- 测试根据产品组获取Envelope ---")
        val groupIEnvelope = EceEnvelope.getByProductGroup("Group I")
        assert(groupIEnvelope != null) {
            "未找到Group I的Envelope"
        }
        assert(groupIEnvelope?.sizeClass == "B2") {
            "Group I应该使用Size Class B2，实际为${groupIEnvelope?.sizeClass}"
        }
        println("✓ Group I对应Size Class B2")

        // 5. 测试根据假人代码获取Envelope
        println("\n--- 测试根据假人代码获取Envelope ---")
        val q3Envelope = EceEnvelope.getByDummyCode("Q3")
        assert(q3Envelope != null) {
            "未找到Q3假人的Envelope"
        }
        assert(q3Envelope?.applicableGroup == "Group I/II") {
            "Q3应该属于Group I/II，实际为${q3Envelope?.applicableGroup}"
        }
        println("✓ Q3对应Group I/II (Size Class B2)")

        // 6. 测试根据身高获取Envelope
        println("\n--- 测试根据身高获取Envelope ---")
        val envelope87cm = EceEnvelope.getByHeight(87)
        assert(envelope87cm != null) {
            "未找到87cm身高的Envelope"
        }
        println("✓ 87cm对应${envelope87cm?.applicableGroup} (Size Class ${envelope87cm?.sizeClass})")

        val envelope40cm = EceEnvelope.getByHeight(40)
        assert(envelope40cm?.sizeClass == "B1" || envelope40cm?.sizeClass == "D") {
            "40cm应该对应Size Class B1或D，实际为${envelope40cm?.sizeClass}"
        }
        println("✓ 40cm对应Size Class ${envelope40cm?.sizeClass}")

        val envelope130cm = EceEnvelope.getByHeight(130)
        assert(envelope130cm?.sizeClass == "E") {
            "130cm应该对应Size Class E，实际为${envelope130cm?.sizeClass}"
        }
        println("✓ 130cm对应Size Class E")

        println("\n========== 所有测试通过 ==========")
    }

    /**
     * 打印Envelope数据摘要
     */
    fun printEnvelopeSummary() {
        println("\n========== ECE Envelope 数据摘要 ==========")
        EceEnvelope.STANDARD_ENVELOPES.forEach { envelope ->
            println("${envelope.sizeClass}: ${envelope.applicableGroup} (${envelope.maxLengthMm}×${envelope.maxWidthMm}×${envelope.maxHeightMm}mm)")
        }
        println("===========================================\n")
    }

    /**
     * 测试边界情况
     */
    fun testBoundaryCases() {
        println("\n========== 边界情况测试 ==========")

        // 测试身高边界
        val boundaries = listOf(
            39 to null,   // 低于最小值
            40 to "B1",   // 最小值
            145 to "E",   // 最大值
            146 to null   // 超过最大值
        )

        boundaries.forEach { (height, expectedClass) ->
            val envelope = EceEnvelope.getByHeight(height)
            if (expectedClass == null) {
                assert(envelope == null) {
                    "身高${height}cm不应该有对应的Envelope"
                }
                println("✓ 身高${height}cm无对应Envelope")
            } else {
                assert(envelope?.sizeClass == expectedClass) {
                    "身高${height}cm应该对应Size Class $expectedClass，实际为${envelope?.sizeClass}"
                }
                println("✓ 身高${height}cm对应Size Class $expectedClass")
            }
        }

        println("=====================================\n")
    }
}

/**
 * 在应用启动时运行测试
 */
fun main() {
    EceEnvelopeDataTest.testStandardEnvelopes()
    EceEnvelopeDataTest.printEnvelopeSummary()
    EceEnvelopeDataTest.testBoundaryCases()
}
