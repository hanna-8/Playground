using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.Model;
using Amazon.Runtime;
using Microsoft.Extensions.Configuration;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using System.Linq;

namespace EmployeeAbilities.Models
{
    public interface IAbilityRepository     
    {
        IEnumerable<Ability> GetFiltered(string query);
        void AddAsync(Ability ability);

        void Remove(string name);
        
        void AddEmployeeToAbility(string ability, string employeeName);
    }

    public class AbilityRepository: IAbilityRepository
    {
        private static List<Ability> abilities;
        private readonly AmazonDynamoDBClient client;

        public AbilityRepository(IConfiguration configuration) 
        {
            abilities = new List<Ability> { 
                new Ability {
                    Name = "Java",
                    Tags = new List<string> { "Programming Language" },
                    Employees = new List<string> { "shortName" }
                },
                new Ability
                {
                    Name = "Oracle Certified Expert, Java EE 5 Web Services Developer (1Z0-862)",
                    Tags = new List<string> { "Certifications" },
                    Employees = new List<string> { "shortName" }
                }
            };
            
            client = CreateClient(configuration["aws-access-key"], configuration["aws-secret-key"]);
        }

        public async void AddAsync(Ability ability)
        {
            var items = new Dictionary<string, AttributeValue> {{ "Name", new AttributeValue { S = ability.Name }}};
            if (ability.Employees.Count > 0) items.Add("Employees", new AttributeValue { SS = ability.Employees });
            if (ability.Tags.Count > 0) items.Add("Tags", new AttributeValue { L = new List<AttributeValue>(from tag in ability.Tags select new AttributeValue(tag))});

            await client.PutItemAsync(
                tableName: "ability",
                item: items
            );
            abilities.Add(ability);
        }

        public void AddEmployeeToAbility(string abilityName, string employeeName)
        {
            abilities.Find(a => a.Name.Equals(abilityName)).Employees.Add(employeeName);
        }

        public IEnumerable<Ability> GetFiltered(string query)
        {
            return (query == null) ? abilities : abilities.FindAll(a => a.Name.Contains(query));
        }

        public void Remove(string name)
        {
            abilities.Remove(abilities.Find(a => a.Name.Equals(name)));
        }

        private AmazonDynamoDBClient CreateClient(string accessKey, string secretKey)
        {
            var credentials = new BasicAWSCredentials(
                accessKey: accessKey,
                secretKey: secretKey);
            
            var config = new AmazonDynamoDBConfig { RegionEndpoint = RegionEndpoint.EUCentral1 };

            return new AmazonDynamoDBClient(credentials, config);
        }
    }
}