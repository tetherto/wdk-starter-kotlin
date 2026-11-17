package to.tether.wdk.starter

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import to.tether.wdk.core.WdkError

class WalletViewModelTest {
    @Test
    fun acceptsPrimitiveBooleanResults() {
        assertTrue(isValidVerificationResult(true))
        assertFalse(isValidVerificationResult(false))
    }

    @Test
    fun rejectsStringAndStructuredResults() {
        assertFalse(isValidVerificationResult("true"))
        assertFalse(isValidVerificationResult("1"))
        assertFalse(isValidVerificationResult(mapOf("valid" to true)))
    }

    @Test
    fun treatsSeedDecryptionFailureAsUnusableCredentials() {
        // Arrange:
        val error = WdkError.RpcError("BAD_REQUEST", "Failed to decrypt seed: Decryption failed")

        // Act:
        val unusable = isUnusableCredentialsError(error)

        // Assert:
        assertTrue(unusable)
    }

    @Test
    fun keepsCredentialsOnOtherWorkletErrors() {
        // Arrange:
        val sameCodeOtherCause = WdkError.RpcError("BAD_REQUEST", "Invalid networks configuration")
        val initFailure = WdkError.RpcError("WDK_MANAGER_INIT", "Failed to load WDK")
        val transportFailure = WdkError.IpcError("Worklet closed")

        // Act:
        val results = listOf(sameCodeOtherCause, initFailure, transportFailure).map { isUnusableCredentialsError(it) }

        // Assert:
        assertFalse(results[0])
        assertFalse(results[1])
        assertFalse(results[2])
    }

    @Test
    fun keepsCredentialsOnErrorsThatDoNotComeFromTheWorklet() {
        // Arrange:
        val error = IllegalStateException("Failed to decrypt seed")

        // Act:
        val unusable = isUnusableCredentialsError(error)

        // Assert:
        assertFalse(unusable)
    }

    @Test
    fun detectsAmountAboveBalanceWithoutLosingPrecision() {
        // Arrange:
        val balanceInWei = "137800000000000000"
        val oneWeiMore = "137800000000000001"

        // Act:
        val exceeds = exceedsBalance(amount = oneWeiMore, balance = balanceInWei)

        // Assert:
        assertTrue(exceeds)
    }

    @Test
    fun allowsAmountUpToTheFullBalance() {
        // Arrange:
        val balanceInSatoshis = "450000"

        // Act:
        val fullBalance = exceedsBalance(amount = "450000", balance = balanceInSatoshis)
        val partOfBalance = exceedsBalance(amount = "1000", balance = balanceInSatoshis)

        // Assert:
        assertFalse(fullBalance)
        assertFalse(partOfBalance)
    }
}
