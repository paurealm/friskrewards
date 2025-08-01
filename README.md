Frisk Rewards adds a new global function slot for every single real advancement (advancements that show some kind of progress at the Advancement Tree, not the functional ones such as the receipe unlockers). The specified function will be triggered for any player that achieves any of these advancements.

Additionally, if the advancement is a `challenge`, another specified function will be called instead.

## Configuration

- `enable_rewards`: if set to `false`, the functions won't trigger.
- `simple_reward`: the identifier of the function to be triggered when completing regular advancements.
- `challenge_reward`: the identifier of the function to be triggered when completing challenges.

### Example

```
enable_rewards = true
simple_reward = "coinrewards:basic_reward"
challenge_reward = "coinrewards:special_reward"
```
