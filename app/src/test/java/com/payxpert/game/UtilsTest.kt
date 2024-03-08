package com.payxpert.game

import com.payxpert.game.Utils.Companion.getNotUsedWord
import com.payxpert.game.Utils.Companion.unmask
import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun unmaskTest1() {

        val ret = unmask('A', "BBBBABBBB","_________")

        Assert.assertEquals("____A____", ret)
    }

    @Test
    fun unmaskTest2() {

        val ret = unmask('A', "ABBBBBBBB","_________")

        Assert.assertEquals("A________", ret)
    }

    @Test
    fun unmaskTest3() {

        val ret = unmask('A', "BBBBBBBBA","_________")

        Assert.assertEquals("________A", ret)
    }

    @Test
    fun unmaskTest4() {

        val ret = unmask('A', "BAAAABBBB","_________")

        Assert.assertEquals("_AAAA____", ret)
    }

    @Test
    fun unmaskTest5() {

        val ret = unmask('A', "BBBBAAAAB","_________")

        Assert.assertEquals("____AAAA_", ret)
    }

    @Test
    fun unmaskTest6() {

        val ret = unmask('A', "AAAAAAAAA","_________")

        Assert.assertEquals("AAAAAAAAA", ret)
    }

    @Test
    fun getNotUsedWordTest1() {

        val usedList: MutableList<String> =
            mutableListOf(
                "Hello",
                "Amazing",
                "Difficult",
                "Interview",
                "Mobile",
                "Android",
                "Always",
                "Payxpert",
//                "Challenge"
            )
        val ret = getNotUsedWord(usedList)
        assert(ret.equals("Challenge"))
    }

    @Test
    fun getNotUsedWordTest2() {

        val usedList: MutableList<String> =
            mutableListOf(
                "Hello",
                "Amazing",
                "Difficult",
                "Interview",
//                "Mobile",
                "Android",
                "Always",
                "Payxpert",
                "Challenge"
            )
        val ret = getNotUsedWord(usedList)
        assert(ret.equals("Mobile"))
    }

    @Test
    fun getNotUsedWordTest3() {

        val usedList: MutableList<String> = mutableListOf()
        getNotUsedWord(usedList)
        Assert.assertEquals(1,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(2,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(3,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(4,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(5,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(6,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(7,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(8,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(9,usedList.size)
        getNotUsedWord(usedList)
        Assert.assertEquals(1,usedList.size)
    }

}