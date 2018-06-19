# License

This project is in pre-release; it is intended to have the Apache license but is not ready for consumption by the general public.

# Summary
*Building a delimited file viewer:* 
Why?  Quickly view delimited file (comma-delimited is the default) 
      *and* autosize columns according to content.
      
* High priority features:
  * Button/Action to "toggle"-autosize (i.e. the user has modified column widths so restore autosize but allow to restore user settings)
  * Modifications to view* should be captured in a way that both displays the mod to the user and lets them selectively enable/disable it.
    For example, if I filter a column, the filter expression should be displayed in a way that I can later disable the filter expression
    ... but!... it should still exist in the UI in case I want to re-enable it (i.e. toggle it for the rest of the "session")
    For example, same goes for sorting on a column
        - drag and drop reorder of sorting priority?
  * Allow SQL-like filtering (i.e. where columnN = expression); 
    - perhaps use DB driver that understands file?
* Medium priority features:
  * Search (see JIDE lib; example here <http://www.tutego.de/java/additional-java-swing-components.htm>)
  * Auto detect date fields
*Low priority features:
  * Customize colors/theme

# Install a local JAR to local repo
```mvn install:install-file 
-Dfile=./nimrodlf-1.2d.jar 
-DgroupId=com.nilo.plaf 
-DartifactId=nimrod 
-Dversion=1.2d 
-Dpackaging=jar
```

# Misc/Notes
pattern for exposing API settings/actions:
checkbox is not inherently an "action" but its paradigm is virtually the same as a button


<https://developmentality.wordpress.com/2012/05/02/glazed-lists-an-essential-java-library-for-lists-and-tables/>

<https://adtmag.com/Articles/2001/12/01/Customize-your-JTable-for-an-enriched-Swing-experience.aspx?Page=2>

<https://www.javaworld.com/article/2077480/core-java/java-tip-116--set-your-table-options----at-runtime-.html>

<https://www.javaworld.com/article/2077465/learn-java/java-tip-102--add-multiple-jtable-cell-editors-per-column.html>

<https://www.javaworld.com/article/2077503/learn-java/java-tip-137--manage-distributed-jtables.html>

<https://mvnrepository.com/artifact/org.swinglabs> <<< download artifacts and try to find source if possible!!!
<https://javalibs.com/artifact/org.swinglabs.swingx/swingx-all>      <<< this has a download tab with a source jar!!!

<http://www.informit.com/articles/article.aspx?p=598024&seqNum=2> <<< swingx datepicker

<http://bluemarine.tidalwave.it/about/>

<https://netbeans.org/community/magazine/html/03/bluemarine/>

Interesting archivable page:
<https://docs.oracle.com/cd/E37975_01/user.111240/e17455/java_swing.htm#OJDUG2093>
