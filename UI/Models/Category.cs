using Avalonia.Media;

namespace Pos.Models
{
    public class Category
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Group {get; set;}
        public SolidColorBrush BgColor {get; set;}
        public SolidColorBrush FgColor {get; set;}
        public string IconColor { get; set; }
        private static readonly string iconBaseUrl = "avares://pos/Assets/Images/";
        public string IconName { get; set; } = "";
        public string Icon { get; set; }

        public Category(
            int id, 
            string name, 
            string group, 
            string bgColor, 
            string iconName,
            string fgColor = "#000000")
        {
            Id = id;
            Name = name;
            Group = group;
            BgColor = new SolidColorBrush(Color.Parse(bgColor));
            FgColor = new SolidColorBrush(Color.Parse(fgColor));
            IconColor = $"* {{ stroke: {fgColor}; }}";
            Icon = iconBaseUrl + iconName;
        }
    }
}