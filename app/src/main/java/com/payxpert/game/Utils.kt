package com.payxpert.game

class Utils {

    companion object {

        fun getNotUsedWord(usedWordsList: MutableList<String>): String {
            if (wordsList.minus(usedWordsList).size == 0) usedWordsList.clear()
            val ret = wordsList.minus(usedWordsList).random()
            usedWordsList.add(ret)
            return ret
        }

        fun unmask(charToTest: Char, currentWord: String, maskedWord: String): String {
            var idx = currentWord.indexOf(charToTest, 0, true)
            var masked = maskedWord
            while (idx >= 0) {
                masked = masked.replaceRange(idx, idx + 1, charToTest.toString())
                idx = currentWord.indexOf(charToTest, idx + 1, true)
            }
            return masked
        }
    }

}