package com.jiuqi.rpa.widgets.extract;

public class ColumnEntity {
        private String name = null;
        private String title = null;
 
        public ColumnEntity(String name, String title)
        {
            this.name = name;
            this.title = title;
        }
 
 
        public void setTitle(String title) 
        {
            this.title = title;
        }
 
 
        public String getTitle() 
        {
            return title;
        }
 
 
        public void setName(String name) 
        {
            this.name = name;
        }
 
 
        public String getName() 
        {
            return name;
        }
        
}
