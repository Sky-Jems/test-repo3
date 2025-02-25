using System;
using System.Collections.Generic;

namespace src.TmpModels
{
    public class Category
    {
        public string name { get; set; }
        public string group {get; set;}
        // color will be removed when Category model is finalized, will be randomized in actual implementation
        public Color color {get; set;}
        public string icon {get; set;}

        public Category(string name, string group, Color color, string icon)
        {
            this.name = name;
            this.group = group;
            this.color = color;
            this.icon = icon;
        }
    }
}