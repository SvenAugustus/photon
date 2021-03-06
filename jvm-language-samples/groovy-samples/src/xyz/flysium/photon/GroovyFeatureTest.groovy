/*
 * MIT License
 *
 * Copyright (c) 2020 SvenAugustus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.flysium.photon
/**
 * feature of Groovy.
 *
 * @author Sven Augustus
 */
class GroovyFeatureTest {

    static void main(args) {
        // 类型推断 （def）
        def message = 12
        println message.class

        // 循环 （for）
        repeat("Hello World", 2)
        repeat("Goodbye sunshine", 4)
        repeat("foo")

        // 集合 （collection）
        testCollection()

        // 映射 （map）
        testMap()

        // 闭包 (closure)
        testClosure()
    }


    static def repeat(val, repeat = 5) {
        for (i in 0..<repeat) {
            println val
        }
    }

    static def testCollection() {
        // collection
        def range = 0..4
        println range.class
        assert range instanceof List

        def coll = ["Groovy", "Java", "Ruby"]
        assert coll instanceof Collection
        assert coll instanceof ArrayList

        // collection add
        coll.add("Python")
        coll << "Smalltalk"
        coll[6] = "Perl"
        print coll
        // 请注意，Groovy 支持操作符重载 —<< 操作符被重载，以支持向集合添加项。还可以通过位置参数直接添加项。
        // 在这个示例中，由于集合中只有四个项，所以 [6] 操作符将 “Perl” 放在最后。请自行输出这个集合并查看效果。

        // collection query
        assert coll[1] == "Java"
        // 如果需要从集合中得到某个特定项，可以通过像上面那样的位置参数获取项。例如，如果想得到第二个项 “ Java”，可以编写下面这样的代码（请记住集合和数组都是从 0 开始）：

        // collection magic
        def numbers = [1, 2, 3, 4]
        assert numbers.join(",") == "1,2,3,4"
        assert [1, 2, 3, 4, 3].count(3) == 2
        // join() 和 count() 只是在任何项列表上都可以调用的众多方便方法中的两个。
        // 分布操作符（spread operator） 是个特别方便的工具，使用这个工具不用在集合上迭代，就能够调用集合的每个项上的方法。

        assert ["JAVA", "GROOVY"] == ["Java", "Groovy"]*.toUpperCase()
        // 请注意 *. 标记。对于以上列表中的每个值，都会调用 toUpperCase()，生成的集合中每个 String 实例都是大写的。
    }

    static def testMap() {
        // map
        def hash = [name: "Andy", "VPN-#": 45]
        // 请注意，Groovy 映射中的键不必是 String。在这个示例中，name 看起来像一个变量，但是在幕后，Groovy 会将它变成 String。

        // map add
        hash.dob = "01/29/76"
        assert hash.dob == "01/29/76"

        // map locate
        assert hash["name"] == "Andy"
        hash["gender"] = "male"
        assert hash.gender == "male"
        assert hash["gender"] == "male"
        // 但是，请注意，在使用 [] 语法从映射获取项时，必须将项作为 String 引用。
    }

    static def testClosure() {
        // closure of collection
        def acoll = ["Groovy", "Java", "Ruby"]
        acoll.each {
            println it
        }

        // closure of map
        def hash = [name: "Andy", "VPN-#": 45]
        hash.each { key, value ->
            println "${key} : ${value}"
        }

        // closure like javascript
        def excite = { word ->
            return "${word}!!"
        }
        assert "Groovy!!" == excite("Groovy")
        assert "Java!!" == excite.call("Java")
    }

}
