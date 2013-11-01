import java.io._
import scala.io.Source
import java.text._
import java.util.Date
import scala.math._

// Input from git log --pretty="%ci" --name-only impl

object GitParser {
   def main(args: Array[String]) {
      println("Following is the content read:" )
      val files = parseGitLog("impl-commits")
      val xml = xmlMerge("result.xml",files)
      val writer = new PrintWriter(new File("result_new.xml" ))
      writer.write(xml)
      writer.close()
      // files.foreach{
      //    case (a,b) =>
      //       print(a)
      //       print(" ")
      //       print(b)
      //       print("\r\n")
      // }
   }
   def xmlMerge(file:String,files:List[(String,String,String)]) = {
      var new_xml = ""
      var source = Source.fromFile( file ).mkString
      val refDate =
        if (!files.isEmpty) {
          files.last._2
        } else {
          ""
        }
      while (source.contains("<time/>")) {
        val end = source.indexOf("<time/>")
        new_xml = new_xml ++ source.take(end)
        new_xml = new_xml ++ "<time>" ++ refDate ++ "</time>"
        source = source.drop(end+7)
      }

      while (source.contains("<name>")) {
         val start = source.indexOf("<name>")
         val end = source.indexOf("</name>")
         val toMatch = source.slice(start+7,end).replaceAll("\\\\","/")
         // println(toMatch)
         val toInsert = files.find{ case(name,date,contributor) => name==toMatch }
            match {
               case Some(value) => (value._2,value._3)
               case None => ("","")
            }
         val seconds_old = secondsDiff(refDate,toInsert._1)
         new_xml = new_xml ++ source.take(end+7)
         new_xml = new_xml ++ "<lastUpdate>" ++ toInsert._1 ++ "</lastUpdate>"
         new_xml = new_xml ++ "<secondsOld>" ++ seconds_old.toString ++ "</secondsOld>"
         new_xml = new_xml ++ "<contributor>" ++ toInsert._2 ++ "</contributor>"
         source = source.drop(end+7)
      }
      new_xml ++ source
   }
   def parseGitLog(file:String):List[(String,String,String)] = {
      var lastDate = ""
      var contributor = ""
      var visited = List[String]()
      var files = List[(String,String,String)]()
      for (line <- Source.fromFile( file ).getLines() ) {
        if (line.length > 3 && (line.substring(0,4) forall Character.isDigit)) {
          lastDate = line
        }
        if (line.length > 2 && (line.substring(0,2) == "##")) {
          contributor = line.drop(2)
        } else {
          if (!visited.contains(line)) {
            visited ::= line
            files ::= (line,lastDate,contributor)
          }
        }
      }
      files
   }
   def secondsDiff(dateAStr:String,dateBStr:String):Int = {
     val format = new java.text.SimpleDateFormat("yyyy-MM-dd H:m:s Z")
     if (dateAStr == "" || dateBStr == "") {
       0
     } else {
      val dateA = format.parse(dateAStr)
      val dateB = format.parse(dateBStr)
      println("A: %d",dateA.getTime)
      println("B: %d",dateB.getTime)
      val diff = (dateA.getTime - dateB.getTime) / 1000
      println(diff)
      diff.toInt
     }
   }
}