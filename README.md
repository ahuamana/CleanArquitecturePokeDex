
# Pokémon Team Creator

Welcome to my Pokémon team builder app! This app is built using Firebase Realtime Database to handle the persistence of data. With this app, you can create multiple teams of Pokémon for each region available. You can choose up to 6 Pokémon per team, with a minimum of 3 Pokémon, and only those available in the specific region.

The app features a screen that shows all the teams created, with the option to modify or delete them. For each team, you can view basic information such as the name, number, type, Pokédex description, and image of the Pokémon. If there is no image available, the app will display an empty state.

In addition, the app includes a feature that allows users to generate a short link or token to share their team with others. The app also uses a custom typography for all screens to improve the overall design.

Finally, the app incorporates Crashlytics, which helps to identify and report any crashes that occur during use, ensuring that the app runs smoothly at all times.

The project follows the Clean Architecture and SOLID principles for better scalability, maintainability, and testability of the codebase.

## Features

- Firebase Realtime Database for data persistence
- Multiple teams creation for each region available
- Team builder with up to 6 Pokémon and minimum 3 Pokémon
- Basic information displayed for each Pokémon: name, number, type, Pokédex description, and image
- Option to modify or delete teams
- Token generation to share teams
- Crashlytics for identifying and reporting crashes

## Screenshots

![pokedex - screenshots](https://user-images.githubusercontent.com/60039961/221394064-eee4b436-65e8-4fab-8191-b77d05e07ad7.png)

## Permissions

On Android versions prior to Android 8.0, Pokémon Team Creator requires the following permissions:
- Full Network Access.

## API Reference
This project uses the PokeAPI v2 GraphQL endpoint as its data source. The endpoint URL is: https://beta.pokeapi.co/graphql/v1beta. This endpoint allows for more flexible queries than the REST API.
#### Query Example

Here's an example GraphQL query that demonstrates how to use the endpoint to retrieve data for your app:

```
  query GetPokemonByRegion($regionName: String!, $limit: Int!, $offset: Int!) {
    pokemon_gen: pokemon_v2_generation(where: {pokemon_v2_region: {name: {_eq: $regionName}}}) {
        pokemons: pokemon_v2_pokemonspecies(limit: $limit, offset: $offset, order_by: {order: asc}) {
            order
            name
            descripcion_form:pokemon_v2_pokemonspeciesflavortexts {
                descripcion:flavor_text
                language_form:pokemon_v2_language {
                    language:iso639
                }
            }
            details:pokemon_v2_pokemons {
                image_form:pokemon_v2_pokemonsprites {
                    image:sprites
                }
                types_form:pokemon_v2_pokemontypes {
                    type:pokemon_v2_type {
                        name
                    }
                }
            }
        }
    }
}
```
This query retrieves a list of Pokemon for a given region name, limit, and offset.
#### Query Variables

The PokeAPI v2 GraphQL endpoint supports query variables, which allow you to pass dynamic values into your queries. This can be helpful when you need to retrieve data based on user input or other dynamic factors.

#### Schema Documentation
For more information about the available types, fields, and operations in the API, check out the PokeAPI v2 GraphQL schema documentation https://beta.pokeapi.co/graphql/console/ .

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `regionName` | `string` | **Required**.  |
| `limit` | `string` | **Required**.  |
| `offset` | `string` | **Required**.  |

#### Firebase Authentication
The app uses Firebase Auth for user authentication with Google or Facebook.

#### Firebase Realtime Database
The app uses Firebase Realtime Database for data persistence.

#### Report Crash with Crashlytics
The app reports crash logs with Crashlytics for live reporting of any exceptions.

#### Firebase Authentication



## Authors

- [@ahuamana](https://www.github.com/ahuamana)

