using System.Text.Json.Serialization;

namespace Pos.Models
{
    public class Staff
    {
        public int Id { get; set; }
        [JsonPropertyName("first_name")]
        public string firstName { get; set; } = "";
        [JsonPropertyName("last_name")]
        public string lastName { get; set; } = "";
        public string AccessToken { get; set; } = "";
        public string FullName => $"{firstName} {lastName}".Trim();
    }
}