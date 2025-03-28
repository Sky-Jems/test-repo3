using System;
using Avalonia.Media;

namespace Pos.Models
{
    public class OptionGroup(int id, string name)
    {
        public int Id { get; set; } = id;
        public string Name { get; set; } = name;
    }
}