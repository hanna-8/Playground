using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
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
        public async Task<IEnumerable<Ability>> GetFiltered([FromQuery]string query) => await abilities.GetFilteredAsync(query);

        // POST api/abilities
        [HttpPost]
        public async Task<IActionResult> AddNewAbility([FromBody]Ability ability) 
        {
            var status = await abilities.AddAsync(ability);
            
            if (status == HttpStatusCode.OK)
                return Ok();
            else
                return NotFound();
        } 

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
