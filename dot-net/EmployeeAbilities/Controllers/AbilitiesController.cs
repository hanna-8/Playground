using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using EmployeeAbilities.Models;
using Microsoft.AspNetCore.Mvc;

namespace EmployeeAbilities.Controllers
{
    [Route("api/[controller]")]
    public class AbilitiesController : Controller
    {
        private IAbilityRepository abilities;

        public AbilitiesController(IAbilityRepository abilities) => this.abilities = abilities;

        // GET api/abilities?query=some-query
        [HttpGet]
        public IEnumerable<Ability> GetFiltered([FromQuery]string query) => abilities.GetFiltered(query);

        // POST api/abilities
        [HttpPost]
        public void AddNewAbility([FromBody]Ability ability) => abilities.AddAsync(ability);

        // POST api/abilities/abilityName
        [HttpPost("{name}")]
        public void AddAbilityToProfile(string name, [FromForm]string level,
            [FromForm]string lastUsed, [FromForm]int experience, [FromForm]string employee) 
        {
            /* if (string.IsNullOrEmpty(ability))
            {
            throw new ArgumentException("message", nameof(ability));
            } */
            abilities.AddEmployeeToAbility(name, employee);
        }

        // DELETE api/abilities/name
        [HttpDelete("{name}")]
        public void RemoveAbility(string name) => abilities.Remove(name);
    }
}
