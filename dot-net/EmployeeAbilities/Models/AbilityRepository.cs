using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.Model;
using Amazon.Runtime;
using Microsoft.Extensions.Configuration;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using System.Linq;
using System.Net;
using System;

namespace EmployeeAbilities.Models
{
    public interface IAbilityRepository     
    {
        Task<IEnumerable<Ability>> GetFilteredAsync(string query);
        Task<HttpStatusCode> AddAsync(Ability ability);

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

        public async Task<HttpStatusCode> AddAsync(Ability ability)
        {
            var items = new Dictionary<string, AttributeValue> 
                {
                    { "name", new AttributeValue { S = ability.Name }},
                    { "employees", new AttributeValue { L = new List<AttributeValue>(from employee in ability.Employees select new AttributeValue(employee))}},
                    { "tags", new AttributeValue { L = new List<AttributeValue>(from tag in ability.Tags select new AttributeValue(tag))}}
                };

            var ret = HttpStatusCode.OK;

            try
            {
                 var response = await client.PutItemAsync(
                    tableName: "ability",
                    item: items
                );
                ret = response.HttpStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Ouch: " + ex.ToString());
            }

            return ret;
            /* abilities.Add(ability); */
        }

        public void AddEmployeeToAbility(string abilityName, string employeeName)
        {
            abilities.Find(a => a.Name.Equals(abilityName)).Employees.Add(employeeName);
        }

        public async Task<IEnumerable<Ability>> GetFilteredAsync(string query)
        {
            var trueAbilities = new List<Ability>();
            try
            {
                var request = new ScanRequest { TableName = "ability" };
                var response = await client.ScanAsync(request);

                response.Items.ForEach(item => 
                    trueAbilities.Add(new Ability 
                    { 
                        Name = item["name"].S,
                        Employees = new List<string> (from atrVal in item["employees"].L select atrVal.S),
                        Tags =  new List<string> (from atrVal in item["tags"].L select atrVal.S)
                    })
                );
            }
            catch (Exception ex)
            {
                Console.WriteLine("Ouch: " + ex.ToString());
            }

            return (query == null) ? trueAbilities : trueAbilities.FindAll(a => a.Name.Contains(query));
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