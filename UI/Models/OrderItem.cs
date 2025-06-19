using System.Collections.Generic;

namespace Pos.Models;

public class OrderItem
{
    public string Name { get; set; }
    public int Price { get; set; }
    public List<Option> Options { get; set; }
}
