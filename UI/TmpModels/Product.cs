using System;
using System.Collections.Generic;

namespace src.TmpModels
{
    public class Product
    {
        public string name { get; set; }
        public string price { get; set; }

        public Product(string name, string price)
        {
            this.name = name;
            this.price = price;
        }
    }
}