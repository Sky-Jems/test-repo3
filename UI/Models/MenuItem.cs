using System;
using Avalonia.Media;

namespace Pos.Models
{
    public class MenuItem(
        int id,
        string name,
        float price,
        string bgColor = "#BFDBFE",
        string fgColor = "#000000"
        )
    {
        public int Id { get; set; } = id;
        public string Name { get; set; } = name;
        public string Price { get; set; } = price.ToString("C");
        public SolidColorBrush BgColor { get; set; } = new SolidColorBrush(Color.Parse(bgColor));
        public SolidColorBrush FgColor { get; set; } = new SolidColorBrush(Color.Parse(fgColor));
    }
}