package imito.ac

import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class ReferentialCorrectnessTests {
    @Test
    fun importsAreCorrect() {
        checkImports("src\\main\\java\\imito.core", listOf("imito.core."))
        checkImports("src\\main\\java\\imito.ac\\entities", listOf(
            "imito.core.",
            "imito.ac.R",
            "imito.ac.entities.",
        ))
    }

    private fun checkImports(folder: String, okTexts: List<String>) {
        val pp = Paths.get(folder)

        val filesWithForbiddenImports = StringBuilder()
        Files.walk(pp)
            .filter { path -> path.toString().endsWith(".kt") }
            .forEach { path ->
                val lines = path.toFile().readLines()
                fun countOf(texts: List<String>) = lines
                    .filter { line -> line.contains("import") }
                    .count { line -> texts.any { line.contains(it) } }

                val countTotal = countOf(listOf("imito."))
                val countOk = countOf(okTexts)
                if (countTotal != countOk) {
                    filesWithForbiddenImports.append("$path\n")
                }
            }
        assertEquals("", filesWithForbiddenImports.toString())
    }
}
