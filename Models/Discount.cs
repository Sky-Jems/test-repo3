namespace POS.Models;

public class Discount
{
    public string Id { get; set; };
    public string Name { get; set; };
    public int Deal { get; set; };
    public Datetime ValidUntil { get; set; };
    public Datetime CreatedAt { get; set; };
}