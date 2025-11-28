

1. Install jmeter (with plugin manager)
```shell 
brew install jmeter
JMETER_HOME="$(brew --prefix jmeter)"
```

2. add plugin jpgc-casutg (Options -> Plugin Manager)

3. setup env

```shell
    source venv/bin/activate
    pip install --break-system-packages -r requirements.txt
    # create fake users in keycloak and save credentials
    bash generate_users.sh
```